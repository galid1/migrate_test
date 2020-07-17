package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.application.UserSignUpService;
import com.galid.card_refund.domains.user.application.request_response.UserSignUpRequest;
import com.galid.card_refund.domains.user.application.request_response.UserSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserSignUpController {
    private final UserSignUpService signUpService;

    @PostMapping(value = "/users/auth")
    public UserSignUpResponse signUp(@ModelAttribute @Valid UserSignUpRequest request) throws IOException {
        return signUpService.signUp(request);
    }
}
