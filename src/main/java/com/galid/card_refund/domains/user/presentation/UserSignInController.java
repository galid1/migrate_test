package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserSignInService;
import com.galid.card_refund.domains.user.service.request_response.UserSignInRequest;
import com.galid.card_refund.domains.user.service.request_response.UserSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserSignInController {
    private final UserSignInService userSignInService;

    @PostMapping("/users/sign-in")
    public UserSignInResponse signIn(@RequestBody @Valid UserSignInRequest userSignInRequest) {
        return userSignInService.signIn(userSignInRequest);
    }
}
