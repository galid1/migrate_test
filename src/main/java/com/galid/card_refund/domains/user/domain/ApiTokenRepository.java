package com.galid.card_refund.domains.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiTokenRepository extends JpaRepository<ApiTokenEntity, Long> {
    Optional<ApiTokenEntity> findFirstByUserId(Long userId);
}
