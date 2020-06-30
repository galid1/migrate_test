package com.galid.card_refund.domains.user.application.request_response;

import lombok.*;

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
