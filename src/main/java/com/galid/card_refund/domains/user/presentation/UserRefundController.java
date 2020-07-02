package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.user.application.UserRefundService;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequest;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequestList;
import com.galid.card_refund.domains.user.application.request_response.UserRefundResponse;
import com.galid.card_refund.domains.user.application.request_response.UserRefundResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
                                            @ModelAttribute UserRefundRequestList request) {
        request.refundItemImageToByte();
        return refundService.refund(userId, request);
    }

    @GetMapping("/users/{userId}/refunds")
    public UserRefundResultResponse getRefundRequestResult(@PathVariable("userId") Long userId) {
        return refundService.getRefundRequestResult(userId);
    }

}
