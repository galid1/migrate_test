package com.galid.card_refund.domains.refund.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundRepository extends JpaRepository<RefundEntity, Long> {
    Optional<RefundEntity> findByRequestorId(Long requestorId);
}
