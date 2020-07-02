package com.galid.card_refund.domains.user.application.request_response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

    public void setRefundItemImageByte() {
        try {
            this.refundItemImageByte = refundItemImage.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
