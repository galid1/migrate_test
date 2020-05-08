package com.galid.card_refund.domains.user.domain;

import com.galid.card_refund.domains.refund.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.card.domain.CardInformation;
import com.galid.card_refund.domains.refund.card.domain.CardInitMoney;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
class UserEntityTest {
    @Autowired
    private UserRepository userRepository;

    private String DEVICE_ID = "123123123123";
    private String CARD_NUM = "1234123412341234";
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
        userEntity.registerCard(CardEntity.builder()
                .cardInformation(new CardInformation(CARD_NUM))
                .initMoney(CardInitMoney.TEN)
                .build());

        //then
        assertThat(userEntity.getCard(), is(not(equalTo(null))));
    }

    @Test
    @Transactional
    public void whenReturnCardThenCardIsNull() throws Exception{
        //given, when
        userEntity.registerCard(CardEntity.builder()
                .cardInformation(new CardInformation(CARD_NUM))
                .initMoney(CardInitMoney.TEN)
                .build());
        userEntity.returnCard();

        //then
        Assertions.assertThrows(IllegalStateException.class, () -> userEntity.getCard());
    }
}