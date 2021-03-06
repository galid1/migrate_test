package com.galid.card_refund.domains.admin.service;

import com.galid.card_refund.common.BaseTestConfig;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.domain.RefundLine;
import com.galid.card_refund.domains.refund.domain.RefundResultLine;
import com.galid.card_refund.domains.refund.domain.RefundStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdminEstimateRefundServiceTest extends BaseTestConfig {
    private Money REFUND_REQUEST_AMOUNT = Money.builder()
            .value(1000)
            .build();

    @Test
    public void 환급평가() throws Exception {
        //given, when
        RefundEntity refundEntity = makeRefundEntity();
        estimate(refundEntity);

        //then
        assertEquals(refundEntity.getRefundStatus(), RefundStatus.ESTIMATED_STATUS);
    }

    private RefundEntity makeRefundEntity() {
        return RefundEntity.builder()
                    .requestRefundLineList(
                            List.of(
                                    RefundLine.builder()
                                        .place("TEST")
                                        .purchaseDateTime("TEST")
                                        .itemImageUrl("TEST")
                                        .paymentAmount(REFUND_REQUEST_AMOUNT)
                                        .build()
                            )
                    )
                    .requestorId(1l)
                    .build();
    }

    private void estimate(RefundEntity refundEntity) {
        List<RefundResultLine> refundableLineList = List.of(
                new RefundResultLine("TEST", REFUND_REQUEST_AMOUNT.getValue())
        );

        refundEntity.estimate(refundableLineList
                            ,"TEST DESC"
                            , "TEST");
    }
}