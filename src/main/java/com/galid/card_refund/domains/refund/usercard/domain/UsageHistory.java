package com.galid.card_refund.domains.refund.usercard.domain;

import com.galid.card_refund.common.model.Money;
import lombok.*;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsageHistory {
    private LocalDate date;
    private String place;
    private Money paymentAmount;
}