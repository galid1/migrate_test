package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserLoginService;
import com.galid.card_refund.domains.user.service.request_response.UserLoginRequest;
import com.galid.card_refund.domains.user.service.request_response.UserLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserLoginController {
    private final UserLoginService userLoginService;

    public UserLoginResponse login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        return userLoginService.login(userLoginRequest);
    }
}
