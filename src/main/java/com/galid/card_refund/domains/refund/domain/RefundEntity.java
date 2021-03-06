package com.galid.card_refund.domains.refund.domain;

import com.galid.card_refund.common.logging.BaseAuditEntity;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.user.exception.NotYetEstimatedException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "refund")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefundEntity extends BaseAuditEntity {
    @Id @GeneratedValue
    private Long refundId;

    private String refundResultBarcodeImageUrl;
    private String unRefundableLineDescription;
    @AttributeOverride(name = "value", column = @Column(name = "total_amount"))
    private Money totalAmount;
    @AttributeOverride(name = "value", column = @Column(name = "expect_refund_amount"))
    private Money expectRefundAmount;
    @Enumerated(value = EnumType.STRING)
    private RefundStatus refundStatus;

    @ElementCollection
    @CollectionTable(
            name = "refund_result_line",
            joinColumns = @JoinColumn(name = "refund_id")
    )
    private List<RefundResultLine> refundResultLineList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "refund_line",
        joinColumns = @JoinColumn(name = "refund_id")
    )
    private List<RefundLine> refundLineList = new ArrayList<>();

    @Column(unique = true)
    private Long requestorId;

    @Builder
    public RefundEntity(List<RefundLine> requestRefundLineList, Long requestorId) {
        this.setRefundLineList(requestRefundLineList);
        this.requestorId = requestorId;
        this.refundStatus = RefundStatus.ESTIMATING_STATUS;
    }

    private void setRefundLineList(List<RefundLine> requestRefundLine) {
        if(requestRefundLine.isEmpty())
            throw new IllegalArgumentException("환급 요청내역이 존재하지 않습니다.");

        this.refundLineList = requestRefundLine;
        this.calculateTotalAmount();
        this.calculateExpectRefundAmount();
    }

    private void calculateTotalAmount() {
        this.totalAmount = Money.builder()
            .value(this.refundLineList.stream()
                    .mapToDouble(refundLine -> refundLine.getPaymentAmount().getValue())
                    .sum())
            .build();
    }

    private void calculateExpectRefundAmount() {
        this.expectRefundAmount = Money.builder()
                .value(Math.floor(this.totalAmount.getValue() * 1 / 11))
                .build();
    }

    // 환급 평가
    public void estimate(List<RefundResultLine> refundResultLineList, String unRefundableLineDescription, String refundResultBarcodeImageUrl) {
        if(this.refundStatus != RefundStatus.ESTIMATING_STATUS)
            throw new IllegalStateException("이미 평가한 환급요청입니다.");

        verifyEstimate(refundResultLineList, unRefundableLineDescription);

        this.refundResultLineList = refundResultLineList;
        this.unRefundableLineDescription = unRefundableLineDescription;
        this.refundResultBarcodeImageUrl = refundResultBarcodeImageUrl;
        this.refundStatus = RefundStatus.ESTIMATED_STATUS;
    }

    // 환급 평가 검증
    private void verifyEstimate(List<RefundResultLine> refundableLineList, String unRefundableLineDescription) {
        if(refundableLineList == null || unRefundableLineDescription == null)
            throw new IllegalArgumentException("환급 가능, 불가능 내역은 필수값입니다.");
    }

    public List<RefundResultLine> getRefundResultLineList() {
        verifyIsEstimated();
        return this.refundResultLineList;
    }

    public String getUnRefundableLineDescription() {
        verifyIsEstimated();
        return this.unRefundableLineDescription;
    }

    public String getRefundResultBarcodeImageUrl() {
        verifyIsEstimated();
        return this.refundResultBarcodeImageUrl;
    }

    // 환급 평가가 완료 되었는지 검증
    private void verifyIsEstimated() {
        if(this.refundStatus == RefundStatus.ESTIMATING_STATUS) {
            throw new NotYetEstimatedException();
        }
    }

}
