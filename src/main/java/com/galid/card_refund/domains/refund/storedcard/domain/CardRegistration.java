package com.galid.card_refund.domains.refund.storedcard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CardRegistration {
    private long userId;
    private long userCardId;
    private String serial;
    private String cardNum;
}
