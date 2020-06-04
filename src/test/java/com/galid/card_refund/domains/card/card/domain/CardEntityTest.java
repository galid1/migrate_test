package com.galid.card_refund.domains.card.card.domain;

import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
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

        CardRegistration cardRegistration = CardRegistration.builder()
                .owner(owner)
                .cardNum(RIGHT_CARD_NUM)
                .serial(serial)
                .build();

        // when
        card.register(cardRegistration);

        // then
        Assertions.assertNotEquals(card.getOwner(), null);
        Assertions.assertEquals(card.getCardStatus(), CardStatus.REGISTERED_STATUS);
    }

    @Test
    @Transactional
    public void whenRegisterUserWithWrongSerialThenThrowException() throws Exception {
        // given
        CardRegistration cardRegistration = CardRegistration.builder()
                .owner(owner)
                .cardNum(RIGHT_CARD_NUM)
                .serial(WRONG_SERIAL)
                .build();

        // when, then
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> card.register(cardRegistration));
    }

    @Test
    @Transactional
    public void whenReturnCardThenCardStateIsUNREGISTERED() throws Exception {
        // given
        String serial = card.getCardInformation().getSerial();
        CardRegistration cardRegistration = CardRegistration.builder()
                .owner(owner)
                .cardNum(RIGHT_CARD_NUM)
                .serial(serial)
                .build();

        card.register(cardRegistration);

        // when
        card.returnCard();

        // then
        Assertions.assertEquals(card.getCardStatus(), CardStatus.RETURNED_STATUS);
    }

}