package com.galid.card_refund.domains.refund.storedcard.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoredCardRepository extends JpaRepository<StoredCardEntity, Long> {
    Optional<StoredCardEntity> findByCardInformation_CardNum(String cardNum);

    Optional<StoredCardEntity> findByOwnerId(Long ownerId);
}
