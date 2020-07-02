package com.galid.card_refund.config;

import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.card.domain.CardInformation;
import com.galid.card_refund.domains.card.domain.CardInitMoney;
import com.galid.card_refund.domains.card.domain.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
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

