package com.galid.card_refund.domains.user.application.request_response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserInformationUpdateRequest {
    @NotBlank
    private String nickname;
    @NotNull
    private byte[] userPassportImageByte;
}
