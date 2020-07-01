package com.galid.card_refund.domains.admin.application.request_response;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRefundEstimateRequest {
    @Size(min = 1)
    private List<RefundEstimateLineRequest> refundEstimateLineList;
    @NotNull
    private String unRefundableLineDescription;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefundEstimateLineRequest {
        @NotBlank
        private String placeAndName;
        @Min(0)
        private double paymentAmount;
    }
}
