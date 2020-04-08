package com.galid.card_refund.domains.user.service.request_response;

import com.galid.card_refund.common.model.Money;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class UnRefundableLineResponse {
    private String unRefundableReason;
    private String place;
    private String refundItemImageUrl;
    private Money paymentAmount;
}
