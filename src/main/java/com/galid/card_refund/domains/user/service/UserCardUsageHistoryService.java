package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.UsageHistory;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UsageHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCardUsageHistoryService {
    private final UserRepository userRepository;

    public List<UsageHistoryResponse> getUsageHistories(Long userId) {
        List<UsageHistory> usageHistoryList = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."))
                .getUsageHistoryList();

        return usageHistoryList.stream()
                .map(usageHistory -> toUsageHistoryResponse(usageHistory))
                .collect(Collectors.toList());
    }

    private UsageHistoryResponse toUsageHistoryResponse(UsageHistory usageHistory) {
        return UsageHistoryResponse.builder()
                .date(usageHistory.getDate())
                .place(usageHistory.getPlace())
                .paymentAmount(usageHistory.getPaymentAmount().getValue())
                .remainAmount(usageHistory.getRemainAmount().getValue())
                .build();
    }

    @Transactional
    public void recordUsageHistory(Long userId, UsageHistory usageHistory) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        userEntity.recordCardUsage(usageHistory);
    }
}
