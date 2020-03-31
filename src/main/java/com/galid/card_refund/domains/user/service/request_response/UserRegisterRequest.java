package com.galid.card_refund.domains.user.service.request_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserRegisterRequest {
    private String deviceId;
    private String nickname;
}
