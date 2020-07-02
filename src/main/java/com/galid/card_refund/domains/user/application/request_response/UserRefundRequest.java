package com.galid.card_refund.domains.user.application.request_response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRefundRequest {
    @NotBlank
    private String place;
    @NotBlank
    private String purchaseDateTime;
    @NotBlank
    private double paymentAmount;

    @Getter(value = AccessLevel.PRIVATE)
    @NotBlank
    private MultipartFile refundItemImage;

    private byte[] refundItemImageByte;

    @Builder
    public UserRefundRequest(@NotBlank String place, @NotBlank String purchaseDateTime, @NotBlank double paymentAmount, @NotBlank MultipartFile refundItemImage) {
        this.place = place;
        this.purchaseDateTime = purchaseDateTime;
        this.paymentAmount = paymentAmount;
        this.refundItemImage = refundItemImage;
    }

    public void setRefundItemImageByte() {
        try {
            this.refundItemImageByte = refundItemImage.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
