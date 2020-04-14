package com.galid.card_refund.domains.refund.card.service;

import com.galid.card_refund.domains.refund.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.card.domain.CardInformation;
import com.galid.card_refund.domains.refund.card.domain.CardRepository;
import com.galid.card_refund.domains.refund.card.service.request_response.CardCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;

    @Transactional
    public Long createCard(CardCreateRequest request) {
        verifyDuplicateCardNum(request);

        CardEntity newCard = CardEntity.builder()
                .cardInformation(new CardInformation(request.getCardNum()))
                .initMoney(request.getCardInitMoney())
                .build();

        return cardRepository.save(newCard).getCardId();
    }

    private void verifyDuplicateCardNum(CardCreateRequest request) {
        if(cardRepository.findByCardInformation_CardNum(request.getCardNum())
            .isPresent())
            throw new IllegalArgumentException("이미 존재하는 카드번호 입니다.");
    }
}
