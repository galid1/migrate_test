package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.aws.ImageType;
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
import org.springframework.mock.web.MockMultipartFile;
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
        when(s3FileUploader.uploadFile("test", ImageType.PASSPORT_IMAGE, null))
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
        UserSignUpResponse signUpResponse = userSignUpService.signUp(request, new MockMultipartFile("TEST", "TEST".getBytes()));
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
                .build();

        UserSignUpRequest requestTwo = UserSignUpRequest.builder()
                .nickname("JUN")
                .deviceId("123123")
                .build();

        MockMultipartFile mockMultipartFile = new MockMultipartFile("TEST", "TEST".getBytes());

        //when, then
        assertThrows(IllegalArgumentException.class, () -> {
            userSignUpService.signUp(requestOne, mockMultipartFile);
            userSignUpService.signUp(requestTwo, mockMultipartFile);
        });
    }
}