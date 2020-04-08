package com.galid.card_refund.domains.refund.card.service;

import com.galid.card_refund.domains.refund.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.card.domain.CardRepository;
import com.galid.card_refund.domains.refund.card.service.request_response.CardRegistrationConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    @Transactional(readOnly = true)
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
}
