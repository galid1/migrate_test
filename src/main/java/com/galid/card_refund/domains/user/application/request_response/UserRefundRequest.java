package com.galid.card_refund.domains.user.application.request_response;

import lombok.*;

import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserRefundRequest {
    @NotNull
    private int refundItemId;
    @NotNull
    private String place;
    @NotNull
    private String purchaseDateTime;
    @NotNull
    private double paymentAmount;
}
