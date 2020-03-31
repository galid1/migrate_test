package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.request_response.UserRegisterRequest;
import com.galid.card_refund.domains.user.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRegisterController {
    private final UserRegisterService userRegisterService;

    @PostMapping("/users")
    public long registerUser(@RequestBody UserRegisterRequest request) {
      return userRegisterService.registerUser(request);
    }
}
