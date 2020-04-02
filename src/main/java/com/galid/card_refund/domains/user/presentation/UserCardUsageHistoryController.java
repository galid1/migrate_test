package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.domain.UsageHistory;
import com.galid.card_refund.domains.user.service.UserCardUsageHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserCardUsageHistoryController {
    private final UserCardUsageHistoryService usageHistoryService;

    @GetMapping("/users/{userId}/usercards")
    public List<UsageHistory> getUsageHistories(@PathVariable("userId") Long userId) {
        return usageHistoryService.getUsageHistories(userId);
    }

}