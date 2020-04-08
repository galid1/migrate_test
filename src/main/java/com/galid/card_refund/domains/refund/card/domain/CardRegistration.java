package com.galid.card_refund.domains.refund.card.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CardRegistration {
    private long userId;
    private String serial;
    private String cardNum;
}
