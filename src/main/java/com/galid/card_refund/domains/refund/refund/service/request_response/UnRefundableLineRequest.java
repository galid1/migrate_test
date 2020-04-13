package com.galid.card_refund.domains.refund.refund.service.request_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnRefundableLineRequest {
    @NotEmpty(message = "환급 불가능 이유는 필수 값 입니다.")
    private String unRefundableReason;
    private String placeAndName;
    private String refundItemImageUrl;
    private double paymentAmount;
}
