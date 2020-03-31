package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRegisterServiceTest {
    @Autowired
    private UserRegisterService userRegisterService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception {
        //given
        UserRegisterRequest request = UserRegisterRequest.builder()
                .nickname("KIM")
                .deviceId("123123")
                .build();
           
        //when
        Long savedId = userRegisterService.registerUser(request);
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
            userRegisterService.registerUser(requestOne);
            userRegisterService.registerUser(requestTwo);
        });
    }
}