package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.application.UserSignUpService;
import com.galid.card_refund.domains.user.application.request_response.UserSignUpRequest;
import com.galid.card_refund.domains.user.application.request_response.UserSignUpResponse;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserSignUpController {
    private final UserSignUpService signUpService;

    @PostMapping(value = "/users/auth", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserSignUpResponse signUp(@RequestParam("information") UserSignUpRequest request,
                                     @RequestParam("image") MultipartFile passportImage) throws IOException {
        System.out.println("sign up @@@@@@@@@");
        return signUpService.signUp(request, passportImage);
    }

}
