package com.galid.card_refund.domains.user.application.request_response;

import com.galid.card_refund.common.model.Money;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserRefundResultResponse {
    private UserInformationDto userInformation;
    private List<RefundResultResponseLine> refundResultResponseLineList;
    private String unRefundableLineDescription;
    private String refundResultBarcodeImageUrl;

    @Data
    @AllArgsConstructor
    public static class RefundResultResponseLine {
        private String place;
        private double paymentAmount;
        private double refundAmount;

        @Builder
        public RefundResultResponseLine(String place, Money paymentAmount, Money refundAmount) {
            this.place = place;
            this.paymentAmount = paymentAmount.getValue();
            this.refundAmount = refundAmount.getValue();
        }
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class UserInformationDto {
        private String name;
        private String nation;
        private String passportNum;
        private String address;
    }
}
