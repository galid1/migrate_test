package com.galid.card_refund.domains.refund.storedcard.service;

import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardEntity;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardRepository;
import com.galid.card_refund.domains.refund.storedcard.service.request_response.CardRegistrationConfirmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoredCardService {
    private final StoredCardRepository storedCardRepository;

    @Transactional(readOnly = true)
    public CardRegistrationConfirmResponse confirmCardRegistration(Long ownerId) {
        StoredCardEntity cardEntity = storedCardRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 등록한 카드정보가 없습니다."));

        return toCardRegistrationConfirmResponse(cardEntity);
    }

    private CardRegistrationConfirmResponse toCardRegistrationConfirmResponse(StoredCardEntity cardEntity) {
        return CardRegistrationConfirmResponse.builder()
                .ownerId(cardEntity.getOwnerId())
                .remainAmount(cardEntity.getRemainAmount().getValue())
                .build();
    }
}
