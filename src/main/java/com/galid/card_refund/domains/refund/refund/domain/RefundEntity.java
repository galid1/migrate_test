package com.galid.card_refund.domains.refund.refund.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import com.galid.card_refund.common.model.Money;
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
public class RefundEntity extends BaseEntity {
    @Id @GeneratedValue
    private Long refundId;

    @Enumerated(value = EnumType.STRING)
    private RefundState refundState;

    private Long requestorId;

    @ElementCollection
    @CollectionTable(
        name = "refund_line",
        joinColumns = @JoinColumn(name = "refund_id")
    )
    private List<RefundLine> refundLineList = new ArrayList<>();

    @AttributeOverride(name = "value", column = @Column(name = "total_amount"))
    private Money totalAmount;
    @AttributeOverride(name = "value", column = @Column(name = "expect_refund_amount"))
    private Money expectRefundAmount;

    @Getter(value = AccessLevel.PRIVATE)
    @ElementCollection
    @CollectionTable(
            name = "refundable_line",
            joinColumns = @JoinColumn(name = "refund_id")
    )
    private List<RefundResultLine> refundResultLineList = new ArrayList<>();

    @Getter(value = AccessLevel.PRIVATE)
    private String unRefundableLineDescription;

    @Builder
    public RefundEntity(List<RefundLine> requestRefundLine, Long requestorId) {
        this.setRefundLineList(requestRefundLine);
        this.requestorId = requestorId;
        this.refundState = RefundState.WAIT;
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

    public void estimate(List<RefundResultLine> refundResultLineList, String unRefundableLineDescription) {
        verifyNotYetEstimate();
        verifyEstimate(refundResultLineList, unRefundableLineDescription);

        this.refundResultLineList = refundResultLineList;

        this.refundState = RefundState.COMPLETE;
    }

    private void verifyEstimate(List<RefundResultLine> refundableLineList, String unRefundableLineDescription) {
        if(refundableLineList == null || unRefundableLineDescription == null)
            throw new IllegalArgumentException("환급 가능, 불가능 내역은 필수값입니다.");
    }

    private void verifyNotYetEstimate() {
        if(this.refundState == RefundState.COMPLETE)
            throw new IllegalStateException("이미 평가한 환급요청입니다.");
    }

    public List<RefundResultLine> getRefundResultLineList() {
        verifyIsEstimated();
        return this.refundResultLineList;
    }

    public String getUnRefundableLineDescription() {
        verifyIsEstimated();
        return this.unRefundableLineDescription;
    }

    private void verifyIsEstimated() {
        if(this.refundState != RefundState.COMPLETE)
            throw new IllegalStateException("아직 평가 되지 않은 환급 요청입니다.");
    }

}
