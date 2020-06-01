package com.galid.card_refund.config;

import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.card.domain.CardInformation;
import com.galid.card_refund.domains.card.domain.CardInitMoney;
import com.galid.card_refund.domains.card.domain.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardSetUp {
    @Autowired
    private CardRepository cardRepository;

    public CardEntity saveCard(CardInformation cardInformation, CardInitMoney initMoney) {
        CardEntity savedCard = cardRepository.save(CardEntity.builder()
                .initMoney(initMoney)
                .cardInformation(cardInformation)
                .build());

        return savedCard;
    }
}

