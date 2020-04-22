package com.galid.card_refund.domains.user.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.domains.user.service.UserCardUsageHistoryService;
import com.galid.card_refund.domains.user.service.request_response.UsageHistoryResponse;
import com.galid.card_refund.domains.user.service.request_response.UsageRecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserCardUsageHistoryController {
    private final UserCardUsageHistoryService usageHistoryService;
    private final ObjectMapper objectMapper;

    @GetMapping("/users/{userId}/user-cards/usage")
    public List<UsageHistoryResponse> getUsageHistories(@PathVariable("userId") Long userId) {
        return usageHistoryService.getUsageHistories(userId);
    }

    @PostMapping("/users/user-cards/usage")
    public void recordUsageHistory(@RequestBody String usageMessage) throws UnsupportedEncodingException, JsonProcessingException {
        String decoded = URLDecoder.decode(usageMessage, "UTF-8");
        String removeLast = decoded.substring(0, decoded.length() - 1);
        UsageRecordRequest usageRecordRequest = objectMapper.readValue(removeLast, UsageRecordRequest.class);

        usageHistoryService.recordUsageHistory(usageRecordRequest);
    }
}
