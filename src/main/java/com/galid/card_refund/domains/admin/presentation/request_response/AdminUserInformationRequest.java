package com.galid.card_refund.domains.admin.presentation.request_response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class AdminUserInformationRequest {
    private String name;
    private String nation;
    private String passportNum;
    private String address;
}
