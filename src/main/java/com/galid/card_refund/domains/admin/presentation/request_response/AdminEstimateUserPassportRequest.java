package com.galid.card_refund.domains.admin.presentation.request_response;

import com.galid.card_refund.domains.user.domain.UserPassportState;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class AdminEstimateUserPassportRequest {
    private UserPassportState estimateResultState;
    private String name;
    private String nation;
    private String passportNum;
    private String address;
}
