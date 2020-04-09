package com.galid.card_refund.domains.refund.card.presentation;

import com.galid.card_refund.domains.refund.card.service.CardService;
import com.galid.card_refund.domains.refund.card.service.request_response.CardRegistrationConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping("/cards/users/{userId}")
    public CardRegistrationConfirmResponse confirmCardRegistration(@PathVariable("userId") Long userId) {
        return cardService.confirmCardRegistration(userId);
    }
}