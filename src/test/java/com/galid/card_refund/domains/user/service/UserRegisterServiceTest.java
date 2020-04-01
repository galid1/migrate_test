package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.file.S3FileUploader;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterRequest;
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
public class UserRegisterServiceTest {
    @Autowired
    private UserRegisterService userRegisterService;
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
        UserRegisterRequest request = UserRegisterRequest.builder()
                .nickname("KIM")
                .deviceId("123123")
                .build();

        //when
        Long savedId = userRegisterService.registerUser(request, null);
        UserEntity findEntity = userRepository.findById(savedId).get();

        //then
        assertEquals(findEntity.getDeviceId(), request.getDeviceId());
        assertEquals(findEntity.getNickname(), request.getNickname());
    }

    @Test
    public void 중복회원_가입_예외() throws Exception {
        //given
        UserRegisterRequest requestOne = UserRegisterRequest.builder()
                .nickname("KIM")
                .deviceId("123123")
                .build();

        UserRegisterRequest requestTwo = UserRegisterRequest.builder()
                .nickname("JUN")
                .deviceId("123123")
                .build();

        //when, then
        assertThrows(IllegalArgumentException.class, () -> {
            userRegisterService.registerUser(requestOne, null);
            userRegisterService.registerUser(requestTwo, null);
        });
    }
}