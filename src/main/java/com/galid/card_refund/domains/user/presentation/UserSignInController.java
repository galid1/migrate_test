package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.application.UserSignInService;
import com.galid.card_refund.domains.user.application.request_response.UserSignInRequest;
import com.galid.card_refund.domains.user.application.request_response.UserSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserSignInController {
    private final UserSignInService userSignInService;

    @PutMapping("/users/auth")
    public UserSignInResponse signIn(@RequestBody @Valid UserSignInRequest userSignInRequest) {
        return userSignInService.signIn(userSignInRequest);
    }
}
