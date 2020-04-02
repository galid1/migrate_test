package com.galid.card_refund.domains.refund.refund.domain;

import com.galid.card_refund.common.model.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RefundEntityTest {
    @Autowired
    private RefundRepository refundRepository;

    @Test
    @Transactional
    public void saveRefundEntityTest() throws Exception {
        // given
        double paymentAmount = 10.0;
        double expectedRefundAmount = Math.floor(paymentAmount * 1 / 11);

        RefundEntity savedEntity = RefundEntity.builder()
                .requestRefundLine(Arrays.asList(new RefundLine[]{
                    RefundLine.builder()
                            .itemImageUrl("TEST")
                            .paymentAmount(new Money(paymentAmount))
                            .place("TEST")
                            .build()
                }))
                .requestorId(1l)
                .build();
        refundRepository.save(savedEntity);

        // when
        RefundEntity findEntity = refundRepository.findById(savedEntity.getRefundId())
                .get();

        // then
        assertEquals(savedEntity.getRefundId(), findEntity.getRefundId());
        assertEquals(savedEntity.getRefundState(), findEntity.getRefundState());
        assertEquals(savedEntity.getRefundState(), RefundState.WAIT);
        assertEquals(savedEntity.getTotalAmount().getValue(), paymentAmount);
        assertEquals(savedEntity.getExpectRefundAmount().getValue(), expectedRefundAmount);
    }

}