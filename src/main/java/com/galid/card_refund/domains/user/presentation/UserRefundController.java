package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.application.UserRefundService;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequest;
import com.galid.card_refund.domains.user.application.request_response.UserRefundResponse;
import com.galid.card_refund.domains.user.application.request_response.UserRefundResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;


@RestController
@RequiredArgsConstructor
public class UserRefundController {
    private final UserRefundService refundService;

    @PostMapping("/users/{userId}/refunds")
    public UserRefundResponse requestRefund(@PathVariable("userId") Long userId,
                                            @RequestParam("refundItemInformationList") List<UserRefundRequest> userRefundRequestList,
                                            @RequestParam Map<String, MultipartFile> refundItemImageMap) {
        if(userRefundRequestList.size() != refundItemImageMap.size())
            throw new IllegalArgumentException("환급요청 내역과, 환급 대상 이미지의 수가 맞지 않습니다.");

        Map<String, byte[]> refundItemImageByteMap = toRefundItemImageByteMap(refundItemImageMap);

        return refundService.refund(userId, userRefundRequestList, refundItemImageByteMap);
    }

    private Map<String, byte[]> toRefundItemImageByteMap(Map<String, MultipartFile> refundItemImageMap) {
        return refundItemImageMap.entrySet().stream()
                .map(e -> {
                    Map.Entry<String, byte[]> byteEntry = null;
                    try {
                        byteEntry = Map.entry(e.getKey(), e.getValue().getBytes());
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    return byteEntry;
                })
                .collect(toMap(
                    e -> e.getKey(),
                    e -> e.getValue()
                ));
    }

    @GetMapping("/users/{userId}/refunds")
    public UserRefundResultResponse getRefundRequestResult(@PathVariable("userId") Long userId) {
        return refundService.getRefundRequestResult(userId);
    }

}
