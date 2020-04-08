package com.galid.card_refund.domains.user.service.request_response;

import com.galid.card_refund.common.model.Money;
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
    private Money paymentAmount;
}
