package com.galid.card_refund.domains.admin.service.request_response;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRefundEstimateRequest {
    private List<RefundEstimateLineRequest> refundEstimateLineList;
    private String unRefundableLineDescription;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefundEstimateLineRequest {
        private String placeAndName;
        private double paymentAmount;
    }
}
