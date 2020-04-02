package com.galid.card_refund.domains.refund.storedcard.presentation;

import com.galid.card_refund.domains.refund.storedcard.service.StoredCardService;
import com.galid.card_refund.domains.refund.storedcard.service.request_response.CardRegistrationConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StoredCardController {
    private final StoredCardService storedCardService;

    @GetMapping("/cards/users/{userId}")
    public CardRegistrationConfirmResponse confirmCardRegistration(@PathVariable("userId") Long userId) {
        return storedCardService.confirmCardRegistration(userId);
    }
}
