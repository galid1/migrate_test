package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpRequest;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class UserSignUpServiceTest {
    @Autowired
    private UserSignUpService userSignUpService;
    @Autowired
    private UserRepository userRepository;

    @MockBean
    private S3FileUploader s3FileUploader;

    @BeforeEach
    public void init() {
        when(s3FileUploader.uploadFile("user", null))
                .thenReturn("test");
    }

    @Test
    public void 회원가입() throws Exception {
        //given
        UserSignUpRequest request = UserSignUpRequest.builder()
                .nickname("KIM")
                .deviceId("123123")
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
                .base64PassPortImage("asd")
                .build();

        UserSignUpRequest requestTwo = UserSignUpRequest.builder()
                .nickname("JUN")
                .deviceId("123123")
                .base64PassPortImage("asd")
                .build();

        //when, then
        assertThrows(IllegalArgumentException.class, () -> {
            userSignUpService.signUp(requestOne);
            userSignUpService.signUp(requestTwo);
        });
    }
}