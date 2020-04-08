package com.galid.card_refund.domains.refund.card.service.request_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CardRegistrationConfirmResponse {
    private Long ownerId;
    private double remainAmount;
}
