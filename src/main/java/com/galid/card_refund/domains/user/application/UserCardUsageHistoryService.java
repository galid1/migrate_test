package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.user.domain.UsageHistory;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.application.request_response.UsageHistoryResponse;
import com.galid.card_refund.domains.user.application.request_response.UsageRecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    public void recordUsageHistory(UsageRecordRequest request) {
        String[] payMessageLines = request.getMessage()
                .split("\n\n")[0]
                .split("\n");

        Long userId = getUserIdByCardNum(payMessageLines[1]
                .substring(payMessageLines[1].length() - 4));

        UsageHistory usageHistory = toUsageHistory(payMessageLines[2], payMessageLines[3], payMessageLines[4]);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        userEntity.recordCardUsage(usageHistory);
    }

    private Long getUserIdByCardNum(String lastFourDigitOfCard) {
        return userRepository.findByLastFourDigitOfCardNum(lastFourDigitOfCard)
                .orElseThrow(() -> new IllegalArgumentException("해당 Card를 등록한 User가 존재하지 않습니다."))
                .getUserId();
    }

    private UsageHistory toUsageHistory(String dateAndPlaceLine, String paymentLine, String remainAmountLine) {
        String[] dateAndPlaceTemp = dateAndPlaceLine.split(" ");

        LocalDateTime date = toLocalDateTime(dateAndPlaceTemp[0].split("/"), dateAndPlaceTemp[1].split(":"));
        String place = dateAndPlaceTemp[2];
        double paymentAmount = Double.parseDouble(removeLastChar(paymentLine));
        double remainAmount = formatRemainAmount(remainAmountLine);

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
