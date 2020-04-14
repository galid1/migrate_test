package com.galid.card_refund.domains.refund.card.presentation;

import com.galid.card_refund.domains.refund.card.service.CardService;
import com.galid.card_refund.domains.refund.card.service.request_response.CardCreateRequest;
import com.galid.card_refund.domains.user.service.request_response.UserCardConfirmResponse;
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
}
