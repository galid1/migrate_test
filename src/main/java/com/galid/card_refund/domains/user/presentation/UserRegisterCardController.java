package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.service.UserRegisterCardService;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRegisterCardController {
    private final UserRegisterCardService userRegisterCardService;

    @PostMapping("/users/{userId}/usercards")
    public void registerCard(@PathVariable("userId")long userId, UserRegisterCardRequest request) {
        userRegisterCardService.registerCard(userId, request);
    }


}
