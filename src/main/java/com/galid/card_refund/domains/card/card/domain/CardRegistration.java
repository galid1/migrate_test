package com.galid.card_refund.domains.card.card.domain;

import com.galid.card_refund.domains.user.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CardRegistration {
    private UserEntity owner;
    private String serial;
    private String cardNum;
}
