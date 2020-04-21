package com.galid.card_refund.domains.refund.card.presentation;

import com.galid.card_refund.domains.refund.card.service.CardService;
import com.galid.card_refund.domains.refund.card.service.request_response.CardCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping("/cards")
    public Result createCard(@RequestBody @Valid CardCreateRequest request) {
        return new Result(cardService.createCard(request));
    }

    @Data
    @AllArgsConstructor
    private class Result {
        private Long cardId;
    }
}
