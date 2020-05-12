package com.galid.card_refund.domains.user.service.request_response;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UserSignUpRequest {
//    @NotNull
    private String deviceId;
//    @NotNull
    private String nickname;
}
