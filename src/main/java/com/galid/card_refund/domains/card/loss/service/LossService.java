package com.galid.card_refund.domains.card.loss.service;

import com.galid.card_refund.domains.card.card.domain.CardEntity;
import com.galid.card_refund.domains.card.loss.domain.LossEntity;
import com.galid.card_refund.domains.card.loss.domain.LossRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LossService {
    private final LossRepository lossRepository;
    private final UserRepository userRepository;

    public Long reportLossCard(Long ownerId) {
        UserEntity userEntity = findLossCardOwner(ownerId);
        CardEntity card = userEntity.getCard();

        card.reportLoss();
        userEntity.returnCard();

        validateDuplicatedLossReport(card);
        return saveLossEntity(card).getLossId();
    }

    private UserEntity findLossCardOwner(Long ownerId) {
        return userRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    private LossEntity saveLossEntity(CardEntity card) {
        LossEntity savedLossEntity = lossRepository.save(LossEntity.builder()
                .lostCard(card)
                .build());

        return savedLossEntity;
    }

    private void validateDuplicatedLossReport(CardEntity lossCard) {
        Optional<LossEntity> lossEntity = lossRepository.findById(lossCard.getCardId());
        if (lossEntity.isPresent())
            throw new IllegalStateException("이미 분실 요청된 카드입니다.");
    }

}
