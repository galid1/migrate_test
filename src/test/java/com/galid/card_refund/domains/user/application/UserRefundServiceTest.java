package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.domain.*;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRefundServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefundRepository refundRepository;
    @Autowired
    private UserRefundService userRefundService;

    private UserEntity savedUser;
    private RefundEntity findRefund;
    private final Money REFUND_REQUEST_AMOUNT = new Money(1000);

    @BeforeEach
    public void init() {
        savedUser = userRepository.save(UserEntity.builder()
                .deviceId("123")
                .nickname("JJY")
                .build());

        List<UserRefundRequest> request = List.of(
                UserRefundRequest.builder()
                        .refundItemId(1)
                        .paymentAmount(REFUND_REQUEST_AMOUNT.getValue())
                        .place("TEST")
                        .purchaseDateTime("date")
                        .build()
        );

        userRefundService.refund(savedUser.getUserId(),
                                 request,
                                 Map.ofEntries(entry("", new String("").getBytes())));

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
    public void 환급요청시_이미지개수가_맞지않는경우_에러() throws Exception {
        //given, when
        List<UserRefundRequest> refundLineList = List.of(
                UserRefundRequest.builder()
                        .place("TEST")
                        .purchaseDateTime("TEST")
                        .paymentAmount(1000)
                        .refundItemId(1)
                        .build(),
                UserRefundRequest.builder()
                        .place("TEST")
                        .purchaseDateTime("TEST")
                        .paymentAmount(1000)
                        .refundItemId(1)
                        .build()
        );

        Map<String, byte[]> refundItemImageByteMap = Map.ofEntries(
            entry("TEST", "TEST".getBytes())
        );

        // Then
        assertThrows(IllegalArgumentException.class
                ,() -> userRefundService.refund(savedUser.getUserId(), refundLineList, refundItemImageByteMap));
    }


}