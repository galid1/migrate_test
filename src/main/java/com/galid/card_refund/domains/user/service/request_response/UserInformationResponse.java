package com.galid.card_refund.domains.user.service.request_response;

import lombok.*;

@AllArgsConstructor
@Builder
@Data
public class UserInformationResponse {
    private String nickname;
    private String passportImageUrl;
}
