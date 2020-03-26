package com.galid.card_refund.domains.refund.refund.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefundEntityTest {
    @Autowired
    private RefundRepository refundRepository;

    private RefundEntity refundEntity;

    @BeforeEach
    public void init() {
        this.refundEntity = RefundEntity.builder()
                .refundLineList(new ArrayList<RefundLine>())
                .build();

        refundRepository.save(refundEntity);
    }
    
    @Test
    @Transactional
    public void saveRefundEntityTest() throws Exception {
        // when
        RefundEntity findEntity = refundRepository.findById(this.refundEntity.getRefundId())
                .get();

        // then
        Assertions.assertEquals(refundEntity.getRefundId(), findEntity.getRefundId());
        Assertions.assertEquals(refundEntity.getRefundState(), findEntity.getRefundState());
    }

    @Test
    public void whenEstimateThenStateIsComplete() throws Exception {
        //given, when
        this.estimateRefundRequest();

        //then
        assertEquals(this.refundEntity.getRefundState(), RefundState.COMPLETE);
    }

    @Test
    public void whenEstimateRepeatThrowException() throws Exception {
        //given
        this.estimateRefundRequest();

        //when, then
        Assertions.assertThrows(IllegalStateException.class, () -> this.estimateRefundRequest());
    }

    private void estimateRefundRequest() {
        this.refundEntity.estimateRefundRequest(new ArrayList<>());
    }
}