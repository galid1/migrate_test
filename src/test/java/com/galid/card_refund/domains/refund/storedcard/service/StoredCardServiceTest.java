package com.galid.card_refund.domains.refund.storedcard.service;

import com.galid.card_refund.domains.refund.storedcard.domain.CardInitMoney;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardEntity;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardInformation;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardRepository;
import com.galid.card_refund.domains.refund.storedcard.service.request_response.CardRegistrationConfirmResponse;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.UserCardService;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class StoredCardServiceTest {
    @Autowired
    private StoredCardService storedCardService;

    @Autowired
    private UserCardService userCardService;
    @Autowired
    private StoredCardRepository storedCardRepository;
    @Autowired
    private UserRepository userRepository;


    private String CARD_NUM = "1234123412341234";
    private CardInitMoney initMoney = CardInitMoney.TEN;

    @Test
    public void 카드등록확인() throws Exception {
        //given
        StoredCardEntity storedCardEntity = StoredCardEntity.builder()
                .cardInformation(StoredCardInformation.builder()
                        .cardNum(CARD_NUM)
                        .build())
                .initMoney(initMoney)
                .build();
        storedCardRepository.save(storedCardEntity);

        UserEntity userEntity = UserEntity.builder()
                .passPortImagePath("TEST")
                .deviceId("TEST")
                .nickname("TEST")
                .build();
        userRepository.save(userEntity);

        userCardService.registerCard(userEntity.getUserId(), new UserRegisterCardRequest(CARD_NUM, storedCardEntity.getCardInformation().getSerial()));

        //when
        CardRegistrationConfirmResponse cardRegistrationConfirmResponse = storedCardService.confirmCardRegistration(userEntity.getUserId());

        //then
        assertEquals(cardRegistrationConfirmResponse.getOwnerId(), userEntity.getUserId());
        assertEquals(cardRegistrationConfirmResponse.getRemainAmount(), initMoney.getAmount().getValue());
    }
}