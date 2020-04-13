package com.galid.card_refund.domains.refund.refund.service.request_response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefundableLineRequest {
    private String placeAndName;
    private String itemImageUrl;
    private double paymentAmount;
}
