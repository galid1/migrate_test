package com.galid.card_refund.domains.user.service.request_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserSignUpRequest {
    @NotNull
    private String deviceId;
    @NotNull
    private String nickname;
//    @NotNull
    private String base64PassPortImage;
}
