package com.galid.card_refund.domains.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PushTokenRepository extends JpaRepository<PushTokenEntity, Long> {
    Optional<PushTokenEntity> findFirstByUserId(Long userId);
    Optional<PushTokenEntity> findById(Long tokenId);
}
