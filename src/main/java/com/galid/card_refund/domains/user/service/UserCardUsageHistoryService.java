package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.UsageHistory;
import com.galid.card_refund.domains.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCardUsageHistoryService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UsageHistory> getUsageHistories(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."))
                .getUsageHistoryList();
    }
}
