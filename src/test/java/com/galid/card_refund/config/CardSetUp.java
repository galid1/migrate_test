package com.galid.card_refund.config;

import com.galid.card_refund.domains.card.card.domain.CardEntity;
import com.galid.card_refund.domains.card.card.domain.CardInformation;
import com.galid.card_refund.domains.card.card.domain.CardInitMoney;
import com.galid.card_refund.domains.card.card.domain.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardSetUp {
    @Autowired
    private CardRepository cardRepository;

    public CardEntity saveCard() {
        return cardRepository.save(CardEntity.builder()
                .initMoney(CardInitMoney.TEN)
                .cardInformation(new CardInformation("1234123412341234"))
                .build());
    }
}

