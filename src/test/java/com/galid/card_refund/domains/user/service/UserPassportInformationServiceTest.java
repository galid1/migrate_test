package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserInformationResponse;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserPassportInformationServiceTest {
    @Autowired
    private UserInformationService service;
    @Autowired
    private UserRepository repository;

    @Test
    public void 유저정보가져오기() throws Exception {
        //given
        UserEntity userEntity = repository.save(UserEntity.builder()
                .deviceId("123")
                .nickname("JJY")
                .passPortImagePath("TEST")
                .build());

        //when
        UserInformationResponse userInformation = service.getUserInformation(userEntity.getUserId());

        //then
        assertEquals(userInformation.getNickname(), "JJY");
        assertEquals(userInformation.getPassportImageUrl(), "TEST");
    }

}