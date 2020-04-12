package com.galid.card_refund.domains.refund.card.service;

import com.galid.card_refund.domains.refund.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.card.domain.CardInformation;
import com.galid.card_refund.domains.refund.card.domain.CardInitMoney;
import com.galid.card_refund.domains.refund.card.domain.CardRepository;
import com.galid.card_refund.domains.refund.card.service.request_response.CardCreateRequest;
import com.galid.card_refund.domains.refund.card.service.request_response.CardRegistrationConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;

    public CardRegistrationConfirmResponse confirmCardRegistration(Long ownerId) {
        CardEntity cardEntity = cardRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 등록한 카드정보가 없습니다."));

        return toCardRegistrationConfirmResponse(cardEntity);
    }

    private CardRegistrationConfirmResponse toCardRegistrationConfirmResponse(CardEntity cardEntity) {
        return CardRegistrationConfirmResponse.builder()
                .ownerId(cardEntity.getOwnerId())
                .remainAmount(cardEntity.getRemainAmount().getValue())
                .build();
    }

    @Transactional
    public Long createCard(CardCreateRequest request) {
        CardEntity newCard = CardEntity.builder()
                .cardInformation(new CardInformation(request.getCardNum()))
                .initMoney(request.getCardInitMoney())
                .build();

        return cardRepository.save(newCard).getCardId();
    }
}
