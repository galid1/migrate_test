package com.galid.card_refund.domains.refund.refund.domain;

import com.galid.card_refund.common.model.Money;
import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefundResultLine {
    private String place;
    @AttributeOverride(name = "value", column = @Column(name = "payment_amount"))
    private Money paymentAmount;
    @AttributeOverride(name = "value", column = @Column(name = "refund_amount"))
    private Money refundAmount;

    @Builder
    public RefundResultLine(String place, double paymentAmount) {
        this.place = place;
        this.paymentAmount = new Money(paymentAmount);
        calculateRefundAmount();
    }

    private void calculateRefundAmount() {
        this.refundAmount = new Money(Math.floor(paymentAmount.getValue() * 1 / 11));
    }
}
