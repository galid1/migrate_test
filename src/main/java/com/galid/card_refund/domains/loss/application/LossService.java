package com.galid.card_refund.domains.loss.application;

import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.card.domain.CardRepository;
import com.galid.card_refund.domains.loss.domain.LossEntity;
import com.galid.card_refund.domains.loss.domain.LossRepository;
import com.galid.card_refund.domains.loss.domain.LossStatus;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LossService {
    private final LossRepository lossRepository;
    private final CardRepository cardRepository;

    @Transactional
    public Long reportLossCard(Long ownerId) {
        CardEntity card = getCardEntityByOwnerId(ownerId);

        validateDuplicatedLossReport(card.getCardId());
        card.reportLoss();
        return saveLossEntity(card.getCardId()).getLossId();
    }

    private LossEntity saveLossEntity(Long lossCardId) {
        LossEntity savedLossEntity = lossRepository.save(LossEntity.builder()
                .lossCardId(lossCardId)
                .build());

        return savedLossEntity;
    }

    private void validateDuplicatedLossReport(Long cardId) {
        if(getCurrentReceiptLossEntity(cardId).isPresent())
            throw new IllegalStateException("이미 요청된 분실신고가 존재합니다.");
    }

    @Transactional
    public Long processLoss(Long ownerId) {
        CardEntity cardEntity = getCardEntityByOwnerId(ownerId);
        LossEntity lossEntity = getCurrentReceiptLossEntity(cardEntity.getCardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카드의 분실신고 접수 내역이 존재하지 않습니다."));
        lossEntity.processLoss();
        return lossEntity.getLossId();
    }

    @Transactional
    public Long cancelLoss(Long ownerId) {
        CardEntity cardEntity = getCardEntityByOwnerId(ownerId);
        LossEntity lossEntity = getCurrentReceiptLossEntity(cardEntity.getCardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카드의 분실신고 접수 내역이 존재하지 않습니다."));
        lossEntity.cancel();
        return lossEntity.getLossId();
    }

    private CardEntity getCardEntityByOwnerId(Long ownerId) {
        return cardRepository.findFirstByOwnerIdOrderByCreatedDateDesc(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 소유한 카드가 존재하지 않습니다."));
    }

    private Optional<LossEntity> getCurrentReceiptLossEntity(Long cardId) {
        return lossRepository.findFirstByLossCardIdAndLossStatus(cardId, LossStatus.RECEIPT_STATUS);
    }
}
