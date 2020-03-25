package com.galid.card_refund.domains.refund.storedcard.domain;

import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class StoredCardEntityTest {
    @Autowired
    private StoredCardRepository storedCardRepository;

    private String TEST_RIGHT_CARD_NUM = "1111111111111111";
    private String TEST_WRONG_CARD_NUM = "2222";

    private String TEST_WRONG_SERIAL = "AAAA";

    private StoredCardEntity savedEntity = null;

    @BeforeEach
    public void init() {
        StoredCardEntity entity = StoredCardEntity.builder()
                .cardInformation(StoredCardInformation.builder()
                        .cardNum(TEST_RIGHT_CARD_NUM)
                        .build()
                )
                .build();

        savedEntity = storedCardRepository.save(entity);
    }

    @Test
    @Transactional
    public void whenCardNumLengthIsNot16ThrowError() throws Exception {
        //given, when, then
        Assertions.assertThrows(IllegalArgumentException.class, () -> StoredCardEntity.builder()
                .cardInformation(StoredCardInformation.builder()
                        .cardNum(TEST_WRONG_CARD_NUM)
                        .build()
                )
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
                .userCardId(1l)
                .userId(1l)
                .cardNum(TEST_RIGHT_CARD_NUM)
                .serial(serial)
                .build();

        // when
        savedEntity.registerUser(cardRegistration);

        // then
        Assertions.assertNotEquals(savedEntity.getOwnerId(), null);
        Assertions.assertNotEquals(savedEntity.getUserCardId(), null);
        Assertions.assertEquals(savedEntity.getCardState(), StoredCardState.REGISTERED);
    }

    @Test
    @Transactional
    public void whenRegisterUserWithWrongSerialThenThrowException() throws Exception {
        // given
        CardRegistration cardRegistration = CardRegistration.builder()
                .userCardId(1l)
                .userId(1l)
                .cardNum(TEST_RIGHT_CARD_NUM)
                .serial(TEST_WRONG_SERIAL)
                .build();

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> savedEntity.registerUser(cardRegistration));
    }

    @Test
    @Transactional
    public void whenReturnCardThenCardStateIsUNREGISTERED() throws Exception {
        // given
        String serial = savedEntity.getCardInformation().getSerial();
        CardRegistration cardRegistration = CardRegistration.builder()
                .userCardId(1l)
                .userId(1l)
                .cardNum(TEST_RIGHT_CARD_NUM)
                .serial(serial)
                .build();

        savedEntity.registerUser(cardRegistration);

        // when
        savedEntity.initCard();

        // then
        Assertions.assertEquals(savedEntity.getCardState(), StoredCardState.UNREGISTERED);
    }

}