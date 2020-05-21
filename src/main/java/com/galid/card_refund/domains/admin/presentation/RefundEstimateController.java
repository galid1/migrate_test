package com.galid.card_refund.domains.admin.presentation;

import com.galid.card_refund.domains.admin.service.RefundEstimateService;
import com.galid.card_refund.domains.admin.presentation.request_response.RefundEstimateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class RefundEstimateController {
    private final RefundEstimateService refundEstimateService;

    @PostMapping("/refunds/{refundId}")
    public void estimateRefundRequest(@PathVariable("refundId") Long refundId,
                                      @RequestParam("estimateInformation") RefundEstimateRequest request,
                                      @RequestParam("barcodeImage") MultipartFile barcodeImage) throws IOException {
        refundEstimateService.estimateRefundRequest(refundId, request, barcodeImage.getBytes());
    }
}
