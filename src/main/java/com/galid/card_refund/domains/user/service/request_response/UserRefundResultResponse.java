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
    private List<RefundResultLine> refundableLineList;
    private String unRefundableLineDescription;

    @Data
    public static class RefundResultLine {
        private String place;
        private double refundAmount;

        public RefundResultLine(String place, Money refundAmount) {
            this.place = place;
            this.refundAmount = refundAmount.getValue();
        }
    }
}
