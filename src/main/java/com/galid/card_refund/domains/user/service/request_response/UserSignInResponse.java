package com.galid.card_refund.domains.user.service.request_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserSignInResponse {
    private Long userId;
    private String apiToken;
}
