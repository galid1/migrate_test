package com.galid.card_refund.domains.user.application.request_response;

import lombok.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInformationResponse {
    private String nickname;
    private String passportImageUrl;
}
