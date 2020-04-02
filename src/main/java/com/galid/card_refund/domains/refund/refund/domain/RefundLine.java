package com.galid.card_refund.domains.refund.refund.domain;

import com.galid.card_refund.common.model.Money;
import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RefundLine {
    private String place;
    private String itemImageUrl;
    private Money paymentAmount;
}
