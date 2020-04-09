package com.galid.card_refund.domains.user.service.request_response;

import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserRefundRequest {
    @NotNull
    private String place;
    @NotNull
    private String base64File;
    @NotNull
    private double paymentAmount;
}
