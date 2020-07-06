package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.common.BaseTestConfig;
import com.galid.card_refund.domains.user.application.request_response.UserSignUpRequest;
import com.galid.card_refund.domains.user.application.request_response.UserSignUpResponse;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserSignUpServiceTest extends BaseTestConfig {
    @Autowired
    private UserSignUpService userSignUpService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        UserSignUpRequest request = UserSignUpRequest.builder()
                .nickname("KIM")
                .deviceId("123123")
                .passportImage(new MockMultipartFile("TEST", "TEST".getBytes()))
                .build();

        //when
        UserSignUpResponse signUpResponse = userSignUpService.signUp(request);
        UserEntity findEntity = userRepository.findById(signUpResponse.getUserId()).get();

        //then
        assertEquals(findEntity.getDeviceId(), request.getDeviceId());
        assertEquals(findEntity.getNickname(), request.getNickname());
    }

    @Test
    public void 중복회원_가입_예외() throws Exception {
        //given
        UserSignUpRequest requestOne = UserSignUpRequest.builder()
                .nickname("KIM")
                .deviceId("123123")
                .passportImage(new MockMultipartFile("TEST", "TEST".getBytes()))
                .build();

        UserSignUpRequest requestTwo = UserSignUpRequest.builder()
                .nickname("JUN")
                .deviceId("123123")
                .passportImage(new MockMultipartFile("TEST", "TEST".getBytes()))
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("TEST", "TEST".getBytes());

        //when, then
        assertThrows(IllegalArgumentException.class, () -> {
            userSignUpService.signUp(requestOne);
            userSignUpService.signUp(requestTwo);
        });
    }
}