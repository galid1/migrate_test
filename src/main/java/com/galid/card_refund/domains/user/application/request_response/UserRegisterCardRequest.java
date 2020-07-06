package com.galid.card_refund.domains.user.application.request_response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRegisterCardRequest {
    @NotEmpty
    private String cardNum;
    @NotEmpty
    private String serial;
}
