package com.galid.card_refund.domains.user.application.request_response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

@NoArgsConstructor
@Getter
@ToString
public class UserSignUpRequest {
    @NotBlank
    private String deviceId;
    @NotBlank
    private String nickname;

    @Getter(value = AccessLevel.PRIVATE)
    @NotBlank
    private MultipartFile passportImage;

    private byte[] passportImageByte;

    @Builder
    public UserSignUpRequest(@NotBlank String deviceId, @NotBlank String nickname, @NotBlank MultipartFile passportImage) {
        this.deviceId = deviceId;
        this.nickname = nickname;
        this.passportImage = passportImage;
    }

    public void setPassportImage(MultipartFile passportImage) {
        this.passportImage = passportImage;
        passportImageToByte();
    }

    public void passportImageToByte() {
        try {
            this.passportImageByte = this.passportImage.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
