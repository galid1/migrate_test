package com.galid.card_refund.domains.user.application.request_response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserInformationUpdateRequest {
    @NotBlank
    private String nickname;

    @Getter(value = AccessLevel.PRIVATE)
    private MultipartFile userPassportImage;

    private byte[] userPassportImageByte;

    public UserInformationUpdateRequest(String nickname, MultipartFile userPassportImage) {
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
