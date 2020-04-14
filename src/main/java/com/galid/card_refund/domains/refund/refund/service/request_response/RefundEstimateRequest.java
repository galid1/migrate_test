package com.galid.card_refund.domains.refund.refund.service.request_response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundEstimateRequest {
    private List<RefundableLineRequest> refundableLineRequestList;
    private List<UnRefundableLineRequest> unRefundableLineRequestList;
}