package com.galid.card_refund.domains.user.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.user.domain.UsageHistory;
import com.galid.card_refund.domains.user.service.UserCardUsageHistoryService;
import com.galid.card_refund.domains.user.service.request_response.UsageHistoryResponse;
import com.galid.card_refund.domains.user.service.request_response.UsageRecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users/{userId}/user-cards/usage")
public class UserCardUsageHistoryController {
    private final UserCardUsageHistoryService usageHistoryService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public List<UsageHistoryResponse> getUsageHistories(@PathVariable("userId") Long userId) {
        return usageHistoryService.getUsageHistories(userId);
    }

    @PostMapping
    public void recordUsageHistory(@PathVariable("userId") Long userId, @RequestBody String usageMessage) throws UnsupportedEncodingException, JsonProcessingException {
        String decoded = URLDecoder.decode(usageMessage, "UTF-8");
        String removeLast = decoded.substring(0, decoded.length() - 1);
        UsageRecordRequest usageRecordRequest = objectMapper.readValue(removeLast, UsageRecordRequest.class);
        UsageHistory usageHistory = toUsageHistory(usageRecordRequest);

        usageHistoryService.recordUsageHistory(userId, usageHistory);
    }


    private UsageHistory toUsageHistory(UsageRecordRequest usageRecordRequest) {
        String realMessage = usageRecordRequest.getMessage().split("\n\n")[0];
        String[] paymentLineList = realMessage.split("\n");

        String[] dateAndPlaceTemp = paymentLineList[2].split(" ");

        LocalDateTime date = toLocalDateTime(dateAndPlaceTemp[0].split("/"), dateAndPlaceTemp[1].split(":"));
        String place = dateAndPlaceTemp[2];
        double paymentAmount = Double.parseDouble(removeLastChar(paymentLineList[3]));
        double remainAmount = formatRemainAmount(paymentLineList[4]);

        return UsageHistory.builder()
                .date(date)
                .place(place)
                .paymentAmount(new Money(paymentAmount))
                .remainAmount(new Money(remainAmount))
                .build();
    }

    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private LocalDateTime toLocalDateTime(String[] monthAndDate, String[] hourAndMin) {
        return LocalDateTime.of(LocalDateTime.now().getYear(),
                Integer.parseInt(monthAndDate[0]),
                Integer.parseInt(monthAndDate[1]),
                Integer.parseInt(hourAndMin[0]),
                Integer.parseInt(hourAndMin[1]));
    }

    private double formatRemainAmount(String remainAmountLine) {
        String amountWonWithRest = remainAmountLine.split(":")[1];
        String amountWithRest = removeLastChar(amountWonWithRest);
        double amount = Double.parseDouble(Arrays.asList(amountWithRest.split(","))
                .stream()
                .reduce("", (init, nextLine) -> init + nextLine));

        return amount;
    }

}
