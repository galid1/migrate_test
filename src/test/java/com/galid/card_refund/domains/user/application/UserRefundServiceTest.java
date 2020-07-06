package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.common.BaseTestConfig;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.domain.*;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequestList;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserRefundServiceTest extends BaseTestConfig {
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

        UserRefundRequestList userRefundRequestList = new UserRefundRequestList();
        userRefundRequestList.setUserRefundRequestList(List.of(
                new UserRefundRequest("TEST", "TEST", 1000, new MockMultipartFile("TEST", "TEST".getBytes()))
        ));

        userRefundService.refund(savedUser.getUserId(), userRefundRequestList);

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
}