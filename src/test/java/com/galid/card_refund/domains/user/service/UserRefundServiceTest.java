package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.refund.domain.*;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserRefundRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRefundServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefundRepository refundRepository;
    @Autowired
    private UserRequestRefundService userRequestRefundService;

    private UserEntity savedUser;
    private RefundEntity findRefund;
    private final Money REFUND_REQUEST_AMOUNT = new Money(1000);

    @BeforeEach
    public void init() {
        savedUser = userRepository.save(UserEntity.builder()
                .deviceId("123")
                .passPortImagePath("asdasd")
                .nickname("JJY")
                .build());

        List<UserRefundRequest> request = Arrays.asList(new UserRefundRequest[]{
                UserRefundRequest.builder()
                        .base64File("TEST")
                        .paymentAmount(REFUND_REQUEST_AMOUNT.getValue())
                        .place("TEST")
                        .build()
        });
        userRequestRefundService.refund(request, savedUser.getUserId());

        findRefund = refundRepository.findByRequestorId(savedUser.getUserId()).get();
    }

    @Test
    public void 환급요청() throws Exception {
        //given , when
        // in before each..

        // then
        assertEquals(findRefund.getTotalAmount(), REFUND_REQUEST_AMOUNT);

        Money expectRefundAmount = new Money(Math.floor(REFUND_REQUEST_AMOUNT.getValue() * 1 / 11));
        assertEquals(findRefund.getExpectRefundAmount(), expectRefundAmount);
    }
    
    @Test
    public void 환급평가() throws Exception {
        //given, when
        estimate();

        //then
        assertEquals(findRefund.getRefundState(), RefundState.COMPLETE);
    }

    private void estimate() {
        List<RefundLine> refundableLineList = Arrays.asList(new RefundLine[] {
                new RefundLine("TEST", "TEST", REFUND_REQUEST_AMOUNT)
        });
        List<UnRefundableLine> unRefundableLineList = Arrays.asList(new UnRefundableLine[] {

        });
        findRefund.estimate(refundableLineList, unRefundableLineList);
    }

    @Test
    public void 환급평가_예외() throws Exception {
        //given
        List<RefundLine> refundableLineList = Arrays.asList(new RefundLine[] {

        });
        List<UnRefundableLine> unRefundableLineList = Arrays.asList(new UnRefundableLine[] {

        });

        //when, then
        assertThrows(IllegalArgumentException.class, () -> findRefund.estimate(refundableLineList, unRefundableLineList));
    }

    @Test
    public void 환급_가능내역_반환() throws Exception {
        //given
        estimate();

        //when
        List<RefundLine> refundableList = findRefund.getRefundableLineList();

        //then
        assertEquals(refundableList.size(), 1);
    }

    @Test
    public void 환급_가능내역_반환_예외() throws Exception {
        //given
        RefundEntity findRefundEntity = refundRepository.findByRequestorId(savedUser.getUserId()).get();

        //when, then
        assertThrows(IllegalStateException.class, () -> findRefundEntity.getRefundableLineList());
    }

    @Test
    public void 환급_불가내역_반환() throws Exception {
        //given
        estimate();

        //when
        List<UnRefundableLine> unRefundableList = findRefund.getUnRefundableLineList();

        //then
        assertEquals(unRefundableList.size(), 0);
    }

    @Test
    public void 환급_불가능_내역_반환_예외() throws Exception {
        //given
        RefundEntity findRefundEntity = refundRepository.findByRequestorId(savedUser.getUserId()).get();

        //when, then
        assertThrows(IllegalStateException.class, () -> findRefundEntity.getUnRefundableLineList());
    }
}