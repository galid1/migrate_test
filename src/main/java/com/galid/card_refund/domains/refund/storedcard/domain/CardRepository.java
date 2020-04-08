package com.galid.card_refund.domains.refund.storedcard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    Optional<CardEntity> findByCardInformation_CardNum(String cardNum);

    Optional<CardEntity> findByOwnerId(Long ownerId);
}
