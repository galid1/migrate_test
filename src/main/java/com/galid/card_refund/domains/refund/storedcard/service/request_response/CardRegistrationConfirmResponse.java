package com.galid.card_refund.domains.refund.storedcard.service.request_response;

import com.galid.card_refund.common.model.Money;
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
