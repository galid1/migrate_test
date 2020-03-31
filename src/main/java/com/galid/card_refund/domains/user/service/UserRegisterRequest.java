package com.galid.card_refund.domains.user.service;

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
