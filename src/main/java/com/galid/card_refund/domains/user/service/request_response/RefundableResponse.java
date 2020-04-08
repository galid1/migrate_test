package com.galid.card_refund.domains.user.service.request_response;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.user.domain.UserInformation;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RefundableResponse {
    private UserInformation userInformation;
    private List<RefundableLineResponse> refundableLineList;

    @Data
    public static class RefundableLineResponse {
        private String itemName;
        private Money price;

        public RefundableLineResponse(String itemName, Money price) {
            this.itemName = itemName;
            this.price = price;
        }
    }
}
