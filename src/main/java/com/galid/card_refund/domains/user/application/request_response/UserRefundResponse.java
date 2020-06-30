package com.galid.card_refund.domains.user.application.request_response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NotEmpty
public class UserRefundResponse {
    private Long refundId;
    private double expectRefundAmount;
}
