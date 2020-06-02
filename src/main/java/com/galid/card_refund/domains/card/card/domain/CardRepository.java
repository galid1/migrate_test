package com.galid.card_refund.domains.card.card.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    Optional<CardEntity> findByCardInformation_CardNum(String cardNum);

    Optional<CardEntity> findByOwnerId(Long ownerId);
}
