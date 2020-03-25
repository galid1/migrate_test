package com.galid.card_refund.domains.refund.storedcard.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoredCardRegisterRequest {
    private String cardNum;
    private String serial;
}
