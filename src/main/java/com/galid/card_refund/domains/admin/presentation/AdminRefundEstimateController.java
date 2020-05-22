package com.galid.card_refund.domains.admin.presentation;

import com.galid.card_refund.domains.admin.service.AdminRefundEstimateService;
import com.galid.card_refund.domains.admin.presentation.request_response.AdminRefundEstimateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AdminRefundEstimateController {
    private final AdminRefundEstimateService adminRefundEstimateService;

    @PostMapping("/admin/refunds/{refundId}")
    public void estimateRefundRequest(@PathVariable("refundId") Long refundId,
                                      @RequestParam("estimateInformation") AdminRefundEstimateRequest request,
                                      @RequestParam("barcodeImage") MultipartFile barcodeImage) throws IOException {
        adminRefundEstimateService.estimateRefundRequest(refundId, request, barcodeImage.getBytes());
    }
}
