package com.galid.card_refund.domains.admin.service.request_response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class UserInformationRequest {
    private String name;
    private String nation;
    private String passportNum;
    private String address;
}
