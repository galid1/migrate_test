package com.galid.card_refund.domains.refund.refund.presentation;

import com.galid.card_refund.domains.refund.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.refund.domain.RefundRepository;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefundController {
    private final RefundRepository refundRepository;

    @GetMapping("/refunds/{refundId}/barcode-image")
    public RefundResultBarcodeImageResponse getRefundResultBarcodeImage(@PathVariable("refundId") Long refundId) {
        RefundEntity refundEntity = refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 환급요청입니다."));

        return new RefundResultBarcodeImageResponse(refundEntity.getRefundResultBarcodeImageUrl());
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    private class RefundResultBarcodeImageResponse {
        private String barcodeImageUrl;
    }

}
