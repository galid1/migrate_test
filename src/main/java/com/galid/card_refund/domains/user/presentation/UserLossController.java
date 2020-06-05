package com.galid.card_refund.domains.user.presentation;

import com.galid.card_refund.domains.card.loss.service.LossService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserLossController {
    private final LossService lossService;

    @PutMapping("/users/{userId}/loss")
    public RequestLossResponse requestLoss(@PathVariable("userId") Long userId) {
        return new RequestLossResponse(lossService.reportLossCard(userId));
    }

    @Data
    @AllArgsConstructor
    public class RequestLossResponse {
        private Long lossId;
    }
}
