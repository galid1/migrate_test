package com.galid.card_refund.domains.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiTokenRepository extends JpaRepository<ApiTokenEntity, Long> {
}
