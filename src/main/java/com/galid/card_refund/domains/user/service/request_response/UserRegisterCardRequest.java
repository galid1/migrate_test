package com.galid.card_refund.domains.user.service.request_response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRegisterCardRequest {
    private String cardNum;
    private String serial;
}
