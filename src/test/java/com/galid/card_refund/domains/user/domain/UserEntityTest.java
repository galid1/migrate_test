package com.galid.card_refund.domains.user.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserEntityTest {
    @Autowired
    private UserRepository userRepository;

    private String DEVICE_ID = "123123123123";
    private String RIGHT_NICKNAME = "JJY";
    private String WRONG_NICKNAME = "X";

    private UserEntity userEntity;

    @BeforeEach
    public void init() {
        userEntity = UserEntity.builder()
                .deviceId(DEVICE_ID)
                .nickname(RIGHT_NICKNAME)
                .build();

        userRepository.save(userEntity);
    }

    @Test
    @Transactional
    public void whenCreateUserWithWrongNicknameThrowException() throws Exception {
        // given, when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> UserEntity.builder()
                .nickname(WRONG_NICKNAME)
                .deviceId(DEVICE_ID)
                .build());
    }

    @Test
    @Transactional
    public void whenRegisterCardThenCardIsNotNull() throws Exception {
        //given, when
        userEntity.registerCard(1l);

        //then
        assertThat(userEntity.getCardId(), is(not(equalTo(null))));
    }

    @Test
    @Transactional
    public void whenReturnCardThenCardIsNull() throws Exception{
        //given, when
        userEntity.registerCard(1l);
        userEntity.returnCard();

        //then
        assertThat(userEntity.getCardId(), is(equalTo(null)));
    }
}