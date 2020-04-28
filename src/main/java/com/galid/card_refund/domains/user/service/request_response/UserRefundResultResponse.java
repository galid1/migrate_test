package com.galid.card_refund.domains.user.service.request_response;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.user.domain.UserInformation;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserRefundResultResponse {
    private UserInformation userInformation;
    private List<RefundResultResponseLine> refundResultResponseLineList;
    private String unRefundableLineDescription;

    @Data
    @AllArgsConstructor
    @Builder
    public static class RefundResultResponseLine {
        private String place;
        private double paymentAmount;
        private double refundAmount;

        public RefundResultResponseLine(String place, Money paymentAmount, Money refundAmount) {
            this.place = place;
            this.paymentAmount = paymentAmount.getValue();
            this.refundAmount = refundAmount.getValue();
        }
    }
}
