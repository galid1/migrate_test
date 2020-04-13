package com.galid.card_refund.domains.refund.card.presentation;

import com.galid.card_refund.domains.refund.card.service.CardService;
import com.galid.card_refund.domains.refund.card.service.request_response.CardCreateRequest;
import com.galid.card_refund.domains.refund.card.service.request_response.CardRegistrationConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping("/cards")
    public Long createCard(@RequestBody @Valid CardCreateRequest request) {
        return cardService.createCard(request);
    }

    @GetMapping("/cards/users/{userId}")
    public CardRegistrationConfirmResponse confirmCardRegistration(@PathVariable("userId") Long userId) {
        return cardService.confirmCardRegistration(userId);
    }
}
