package com.galid.card_refund.domains.user.application.request_response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class UserSignUpRequest {
    @NotBlank
    private String deviceId;
    @NotBlank
    private String nickname;
    @NotBlank
    private MultipartFile passportImage;
}
