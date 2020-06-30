package com.galid.card_refund.domains.user.application.request_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserCardConfirmResponse {
    private Long ownerId;
    private double remainAmount;
}
