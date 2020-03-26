package com.galid.card_refund.domains.refund.refund.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @ElementCollection
    @CollectionTable(
        name = "refund_line",
        joinColumns = @JoinColumn(name = "refund_id")
    )
    private List<RefundLine> refundLineList;

    @Builder
    public RefundEntity(List<RefundLine> estimatedRefundLineList) {
        this.refundLineList = estimatedRefundLineList;
        this.refundState = RefundState.WAIT;
    }

    public void estimateRefundRequest(List<RefundLine> refundLineList) {
        verifyNotYetEstimate();
        this.refundLineList = refundLineList;
        this.refundState = RefundState.COMPLETE;
    }

    private void verifyNotYetEstimate() {
        if(this.refundState == RefundState.COMPLETE)
            throw new IllegalStateException("이미 평가한 환급요청입니다.");
    }
}
