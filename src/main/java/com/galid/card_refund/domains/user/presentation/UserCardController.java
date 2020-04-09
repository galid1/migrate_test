package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.user.service.UserCardService;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/usercards")
public class UserCardController {
    private final UserCardService userCardService;

    @GetMapping
    public Money checkRegistered(@PathVariable("userId")long userId) {
        return userCardService.checkRegistered(userId);
    }

    @PostMapping
    public void registerCard(@PathVariable("userId")long userId, UserRegisterCardRequest request) {
        userCardService.registerCard(userId, request);
    }

    @PutMapping
    public void returnCard(@PathVariable("userId")long userId) {
        userCardService.returnCard(userId);
    }
}
