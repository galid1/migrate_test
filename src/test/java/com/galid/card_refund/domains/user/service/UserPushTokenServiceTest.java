package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.user.domain.PushTokenRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.service.request_response.StorePushTokenRequest;
import com.galid.card_refund.domains.user.service.request_response.UpdatePushTokenRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserPushTokenServiceTest {
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private UserPushTokenService pushTokenService;
    @Autowired
    private PushTokenRepository pushTokenRepository;

    private String TEST_PUSH_TOKEN = "TEST";
    private UserEntity TEST_USER_ENTITY;
    private Long TEST_USER_ID;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
        TEST_USER_ID = TEST_USER_ENTITY.getUserId();
    }

    @Test
    public void 토큰저장() throws Exception {
        //given
        StorePushTokenRequest request = new StorePushTokenRequest(TEST_PUSH_TOKEN);

        //when
        pushTokenService.storeUserPushToken(TEST_USER_ID, request);

        //then
        String findPushToken = pushTokenRepository.findByUserId(TEST_USER_ID)
                .get()
                .getPushToken();


        assertEquals(findPushToken, TEST_PUSH_TOKEN);
        assertTrue(findPushToken.length() > 1);
    }

    @Test
    public void 토큰갱신() throws Exception {
        //given
        userSetUp.savePushToken(TEST_USER_ENTITY, TEST_PUSH_TOKEN);
        String NEW_PUSH_TOKEN = "NEW";
        UpdatePushTokenRequest request = new UpdatePushTokenRequest(NEW_PUSH_TOKEN);

        //when
        pushTokenService.updatePushToken(TEST_USER_ID, request);

        //then
        String findPushToken = pushTokenRepository.findByUserId(TEST_USER_ID)
                .get()
                .getPushToken();

        assertEquals(findPushToken, NEW_PUSH_TOKEN);
    }

    @Test
    public void 토큰조회() throws Exception {
        //given
        userSetUp.savePushToken(TEST_USER_ENTITY, TEST_PUSH_TOKEN);

        //when
        String findPushToken = pushTokenService.getPushToken(TEST_USER_ID);

        //then
        assertEquals(findPushToken, TEST_PUSH_TOKEN);
    }

}