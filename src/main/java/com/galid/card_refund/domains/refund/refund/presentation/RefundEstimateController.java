package com.galid.card_refund.domains.refund.refund.presentation;

import com.galid.card_refund.domains.refund.refund.service.RefundEstimateService;
import com.galid.card_refund.domains.refund.refund.service.request_response.RefundEstimateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RefundEstimateController {
    private final RefundEstimateService refundEstimateService;

    @PostMapping("/refunds/{refundId}")
    public void estimateRefundRequest(@PathVariable("refundId") Long refundId, @RequestBody @Valid RefundEstimateRequest request) {
        refundEstimateService.estimateRefundRequest(refundId, request);
    }
}
