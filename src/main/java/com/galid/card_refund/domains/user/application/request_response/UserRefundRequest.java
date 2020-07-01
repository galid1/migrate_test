package com.galid.card_refund.domains.user.application.request_response;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserRefundRequest {
    @NotNull
    private int refundItemId;
    @NotBlank
    private String place;
    @NotBlank
    private String purchaseDateTime;
    @NotBlank
    private double paymentAmount;
}
