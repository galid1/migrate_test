package com.galid.card_refund.domains.admin.application.request_response;

import com.galid.card_refund.domains.user.domain.UserPassportStatus;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class AdminEstimateUserPassportRequest {
    @NotNull
    private UserPassportStatus estimateResultStatus;
    @NotBlank
    private String name;
    @NotBlank
    private String nation;
    @NotBlank
    private String passportNum;
    @NotBlank
    private String address;
}
