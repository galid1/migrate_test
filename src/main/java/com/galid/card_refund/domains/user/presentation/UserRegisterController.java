package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserRegisterRequest;
import com.galid.card_refund.domains.user.service.UserRegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRegisterController {
    private UserRegisterService userRegisterService;

    public UserRegisterController(UserRegisterService userRegisterService) {
        this.userRegisterService = userRegisterService;
    }

    @PostMapping("/users")
    public long registerUser(@RequestBody UserRegisterRequest request) {
      return userRegisterService.registerUser(request);
    }
}
