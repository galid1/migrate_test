package com.galid.card_refund.domains.card.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CardEntityTest {
    @Autowired
    private CardRepository cardRepository;

    private String RIGHT_CARD_NUM = "1111111111111111";
    private String WRONG_CARD_NUM = "2222";

    private String WRONG_SERIAL = "AAAA";

    private CardEntity savedEntity = null;

    @BeforeEach
    public void init() {
        CardEntity entity = CardEntity.builder()
                .cardInformation(new CardInformation(RIGHT_CARD_NUM))
                .initMoney(CardInitMoney.TEN)
                .build();

        savedEntity = cardRepository.save(entity);
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
        String oldSerial = savedEntity.getCardInformation().getSerial();
        savedEntity.getCardInformation().renewSerial();
        String newSerial = savedEntity.getCardInformation().getSerial();

        //then
        Assertions.assertNotEquals(oldSerial, newSerial);
    }

    @Test
    @Transactional
    public void whenRegisterUserTest() throws Exception {
        // given
        String serial = savedEntity.getCardInformation().getSerial();

        CardRegistration cardRegistration = CardRegistration.builder()
                .userId(1l)
                .cardNum(RIGHT_CARD_NUM)
                .serial(serial)
                .build();

        // when
        savedEntity.register(cardRegistration);

        // then
        Assertions.assertNotEquals(savedEntity.getOwnerId(), null);
        Assertions.assertEquals(savedEntity.getCardStatus(), CardStatus.REGISTERED_STATUS);
    }

    @Test
    @Transactional
    public void whenRegisterUserWithWrongSerialThenThrowException() throws Exception {
        // given
        CardRegistration cardRegistration = CardRegistration.builder()
                .userId(1l)
                .cardNum(RIGHT_CARD_NUM)
                .serial(WRONG_SERIAL)
                .build();

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> savedEntity.register(cardRegistration));
    }

    @Test
    @Transactional
    public void whenReturnCardThenCardStateIsUNREGISTERED() throws Exception {
        // given
        String serial = savedEntity.getCardInformation().getSerial();
        CardRegistration cardRegistration = CardRegistration.builder()
                .userId(1l)
                .cardNum(RIGHT_CARD_NUM)
                .serial(serial)
                .build();

        savedEntity.register(cardRegistration);

        // when
        savedEntity.returnCard();

        // then
        Assertions.assertEquals(savedEntity.getCardStatus(), CardStatus.UNREGISTERED_STATUS);
    }

}