package com.galid.card_refund.domains.user.application.request_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInRequest {
    private String deviceId;
}
