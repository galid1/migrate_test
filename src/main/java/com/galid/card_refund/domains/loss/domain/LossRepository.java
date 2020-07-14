package com.galid.card_refund.domains.loss.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LossRepository extends JpaRepository<LossEntity, Long> {
    Optional<LossEntity> findFirstByLossCardIdAndLossStatus(Long cardId, LossStatus status);
}
