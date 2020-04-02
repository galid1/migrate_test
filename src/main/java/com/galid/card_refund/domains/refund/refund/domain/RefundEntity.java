package com.galid.card_refund.domains.refund.refund.domain;

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
public class RefundEntity {
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

    @ElementCollection
    @CollectionTable(
            name = "refundable_line",
            joinColumns = @JoinColumn(name = "refund_id")
    )
    private List<RefundLine> refundableLineList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
        name = "un_refundable_line",
        joinColumns = @JoinColumn(name = "refund_id")
    )
    private List<UnRefundableLine> unRefundableLineList = new ArrayList<>();

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

    public void estimateRefundRequest(List<RefundLine> refundableLineList, List<UnRefundableLine> unRefundableLineList) {
        verifyNotYetEstimate();
        if(refundableLineList == null || unRefundableLineList == null)
            throw new IllegalArgumentException("환급 가능, 불가능 내역은 필수값입니다.");
        this.refundableLineList = refundableLineList;

        this.refundState = RefundState.COMPLETE;
    }

    private void verifyNotYetEstimate() {
        if(this.refundState == RefundState.COMPLETE)
            throw new IllegalStateException("이미 평가한 환급요청입니다.");
    }
}
