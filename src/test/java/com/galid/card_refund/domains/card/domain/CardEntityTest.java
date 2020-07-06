package com.galid.card_refund.domains.card.domain;

import com.galid.card_refund.common.BaseTestConfig;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CardEntityTest extends BaseTestConfig {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;

    private String RIGHT_CARD_NUM = "1111111111111111";
    private String WRONG_CARD_NUM = "2222";

    private String WRONG_SERIAL = "AAAA";

    private CardEntity card = null;
    private UserEntity owner = null;

    @BeforeEach
    public void init() {
        CardEntity entity = CardEntity.builder()
                .cardInformation(new CardInformation(RIGHT_CARD_NUM))
                .initMoney(CardInitMoney.TEN)
                .build();
        card = cardRepository.save(entity);

        UserEntity owner = UserEntity.builder()
                .nickname("TEST")
                .deviceId("TEST")
                .build();

        this.owner = userRepository.save(owner);
    }

    @Test
    @Transactional
    public void whenCardNumLengthIsNot16ThrowError() throws Exception {
        //given, when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> CardEntity.builder()
                .cardInformation(new CardInformation(WRONG_CARD_NUM))
                .build());
    }

    @Test
    @Transactional
    public void whenRenewSerialCreatedNotDuplicatedSerial() throws Exception {
        // given, when
        String oldSerial = card.getCardInformation().getSerial();
        card.getCardInformation().renewSerial();
        String newSerial = card.getCardInformation().getSerial();

        //then
        Assertions.assertNotEquals(oldSerial, newSerial);
    }

    @Test
    @Transactional
    public void whenRegisterUserTest() throws Exception {
        // given
        String serial = card.getCardInformation().getSerial();
        // when
        card.register(owner.getUserId(), serial);
        // then
        Assertions.assertNotEquals(card.getOwnerId(), null);
        Assertions.assertEquals(card.getCardStatus(), CardStatus.REGISTERED_STATUS);
    }

    @Test
    @Transactional
    public void whenRegisterUserWithWrongSerialThenThrowException() throws Exception {
        // when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> card.register(owner.getUserId(), WRONG_SERIAL));
    }

    @Test
    @Transactional
    public void whenReturnCardThenCardStateIsUNREGISTERED() throws Exception {
        // given
        String serial = card.getCardInformation().getSerial();

        card.register(owner.getUserId(), serial);

        // when
        card.returnCard();

        // then
        Assertions.assertEquals(card.getCardStatus(), CardStatus.RETURNED_STATUS);
    }

}