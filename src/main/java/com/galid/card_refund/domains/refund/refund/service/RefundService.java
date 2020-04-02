package com.galid.card_refund.domains.refund.refund.service;

import com.galid.card_refund.domains.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefundService {
    private final UserRepository userRepository;

}
