package com.galid.card_refund.domains.user.application.request_response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserInformationUpdateRequest {
    @NotBlank
    private String nickname;

    @Getter(value = AccessLevel.PRIVATE)
    @NotNull
    private MultipartFile userPassportImage;

    private byte[] userPassportImageByte;

    public UserInformationUpdateRequest(@NotBlank String nickname, @NotNull MultipartFile userPassportImage) {
        this.nickname = nickname;
        this.userPassportImage = userPassportImage;
    }

    public void setUserPassportImage(MultipartFile userPassportImage) {
        this.userPassportImage = userPassportImage;
        userPassportImageToByte();
    }

    public void userPassportImageToByte() {
        try {
            this.userPassportImageByte = this.userPassportImage.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
