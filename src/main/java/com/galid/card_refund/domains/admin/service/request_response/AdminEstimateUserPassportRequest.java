package com.galid.card_refund.domains.admin.service.request_response;

import com.galid.card_refund.domains.user.domain.UserPassportStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class AdminEstimateUserPassportRequest {
    private UserPassportStatus estimateResultStatus;
    private String name;
    private String nation;
    private String passportNum;
    private String address;
}
