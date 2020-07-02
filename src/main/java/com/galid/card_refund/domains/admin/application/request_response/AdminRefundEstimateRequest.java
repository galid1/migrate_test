package com.galid.card_refund.domains.admin.application.request_response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;

@Getter
@NoArgsConstructor
public class AdminRefundEstimateRequest {
    @Size(min = 1)
    private List<RefundEstimateLineRequest> refundEstimateLineList;
    @NotNull
    private String unRefundableLineDescription;

    @Getter(value = AccessLevel.PRIVATE)
    @NotBlank
    private MultipartFile barcodeImage;

    private byte[] barcodeImageByte;

    @Builder
    public AdminRefundEstimateRequest(List<RefundEstimateLineRequest> refundEstimateLineList,
                                      String unRefundableLineDescription,
                                      MultipartFile barcodeImage) {
        this.refundEstimateLineList = refundEstimateLineList;
        this.unRefundableLineDescription = unRefundableLineDescription;
        this.barcodeImage = barcodeImage;
    }

    public void setBarcodeImage(MultipartFile barcodeImage) {
        this.barcodeImage = barcodeImage;
        barcodeImageToByte();
    }

    public void barcodeImageToByte() {
        try {
            this.barcodeImageByte = barcodeImage.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefundEstimateLineRequest {
        @NotBlank
        private String placeAndName;
        @Min(0)
        private double paymentAmount;

    }
}
