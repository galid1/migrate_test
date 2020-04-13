package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.common.config.validation.CustomCollectionValidator;
import com.galid.card_refund.domains.user.service.UserRequestRefundService;
import com.galid.card_refund.domains.user.service.request_response.RefundableResponse;
import com.galid.card_refund.domains.user.service.request_response.UnRefundableLineResponse;
import com.galid.card_refund.domains.user.service.request_response.UserRefundRequest;
import com.galid.card_refund.domains.user.service.request_response.UserRefundResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRequestRefundController {
    private final UserRequestRefundService refundService;
    private final CustomCollectionValidator customCollectionValidator;

    @PostMapping("/users/{userId}/refunds")
    public UserRefundResponse requestRefund(@PathVariable("userId") Long userId, @RequestBody @Valid List<UserRefundRequest> userRefundRequestList, BindingResult bindingResult) {
        customCollectionValidator.validate(userRefundRequestList, bindingResult);

        if(bindingResult.hasErrors())
            throw new IllegalArgumentException("환급 요청 데이터를 모두 입력해야합니다.");

       return refundService.refund(userRefundRequestList, userId);
    }

    @GetMapping("/users/{userId}/refunds/refundable")
    public RefundableResponse getRefundableLineList(@PathVariable("userId") Long userId) {
        return refundService.getRefundable(userId);
    }

    @GetMapping("/user/{userId}/refunds/un-refundable")
    public List<UnRefundableLineResponse> getUnRefundableLineList(@PathVariable("userId") Long userId) {
        return refundService.getUnRefundable(userId);
    }

}
