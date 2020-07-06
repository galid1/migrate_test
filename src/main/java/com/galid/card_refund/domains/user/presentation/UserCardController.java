package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.application.UserCardService;
import com.galid.card_refund.domains.user.application.request_response.UserCardConfirmResponse;
import com.galid.card_refund.domains.user.application.request_response.UserRegisterCardRequest;
import com.galid.card_refund.domains.user.application.request_response.UserRegisterCardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/user-cards")
public class UserCardController {
    private final UserCardService userCardService;

    @GetMapping
    public UserCardConfirmResponse confirmCardRegistration(@PathVariable("userId") Long userId) {
        return userCardService.getCardRegistrationStatus(userId);
    }

    @PostMapping
    public UserRegisterCardResponse registerCard(@PathVariable("userId")long userId, @RequestBody @Valid UserRegisterCardRequest request) {
        return userCardService.registerCard(userId, request);
    }

    @PutMapping
    public void returnCard(@PathVariable("userId")long userId) {
        userCardService.returnCard(userId);
    }
}
