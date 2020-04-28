package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserSignUpService;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpRequest;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class UserSignUpController {
    private final UserSignUpService signUpService;

    @PostMapping("/users")
    public UserSignUpResponse signUp(@RequestBody UserSignUpRequest request) throws IOException {
        return signUpService.signUp(request);
    }


}
