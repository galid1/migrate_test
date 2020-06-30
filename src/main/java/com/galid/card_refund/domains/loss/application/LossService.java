package com.galid.card_refund.domains.loss.application;

import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.card.domain.CardRepository;
import com.galid.card_refund.domains.loss.domain.LossEntity;
import com.galid.card_refund.domains.loss.domain.LossRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LossService {
    private final LossRepository lossRepository;
    private final CardRepository cardRepository;

    public Long reportLossCard(Long ownerId) {
        CardEntity card = cardRepository.findFirstByOwnerIdOrderByCreatedDateDesc(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 소유한 카드가 존재하지 않습니다."));

        validateDuplicatedLossReport(card);
        card.reportLoss();
        return saveLossEntity(card.getCardId()).getLossId();
    }

    private LossEntity saveLossEntity(Long lossCardId) {
        LossEntity savedLossEntity = lossRepository.save(LossEntity.builder()
                .lossCardId(lossCardId)
                .build());

        return savedLossEntity;
    }

    private void validateDuplicatedLossReport(CardEntity lossCard) {
        Optional<LossEntity> lossEntity = lossRepository.findById(lossCard.getCardId());
        if (lossEntity.isPresent())
            throw new IllegalStateException("이미 분실 요청된 카드입니다.");
    }

}
