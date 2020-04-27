package com.galid.card_refund.domains.user.service.request_response;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


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
    private String purchaseDateTime;
    @NotNull
    private double paymentAmount;
}
