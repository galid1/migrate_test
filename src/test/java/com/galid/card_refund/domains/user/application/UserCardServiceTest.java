package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.common.BaseTestConfig;
import com.galid.card_refund.domains.card.domain.*;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.application.request_response.UserCardConfirmResponse;
import com.galid.card_refund.domains.user.application.request_response.UserRegisterCardRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserCardServiceTest extends BaseTestConfig {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserCardService userCardService;

    private String STORED_CARD_NUM = "1234123412341234";

    @Test
    public void 카드등록() throws Exception {
        //given
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .deviceId("123")
                .nickname("KIM")
                .build());

        CardEntity savedStoredCard = cardRepository.save(CardEntity.builder()
                .cardInformation(new CardInformation(STORED_CARD_NUM))
                .initMoney(CardInitMoney.TEN)
                .build());

        UserRegisterCardRequest userRegisterCardRequest = new UserRegisterCardRequest(STORED_CARD_NUM, savedStoredCard.getCardInformation().getSerial());


        //when
        userCardService.registerCard(savedUser.getUserId(), userRegisterCardRequest);


        //then
        assertEquals(savedUser.getUserId(), savedStoredCard.getOwnerId());
    }

    @Test
    public void 존재하지_않는사용자_카드등록_예외() throws Exception {
        //given
        CardEntity savedStoredCard = CardEntity.builder()
                .cardInformation(new CardInformation(STORED_CARD_NUM))
                .initMoney(CardInitMoney.TEN)
                .build();

        cardRepository.save(savedStoredCard);

        UserRegisterCardRequest userRegisterCardRequest = new UserRegisterCardRequest(STORED_CARD_NUM, savedStoredCard.getCardInformation().getSerial());

        //when, then
        long notExistUserId = 0l;
        assertThrows(IllegalArgumentException.class, () -> userCardService.registerCard(notExistUserId, userRegisterCardRequest));
    }
    
    @Test
    public void 존재하지_않는_저장된_카드등록_예외() throws Exception {
        //given
        Long savedUserId = userRepository.save(UserEntity.builder()
                .nickname("KIM")
                .deviceId("123")
                .build()).getUserId();

        UserRegisterCardRequest userRegisterCardRequest = new UserRegisterCardRequest(STORED_CARD_NUM, "1234");

        //when, then
        assertThrows(IllegalArgumentException.class, () -> userCardService.registerCard(savedUserId, userRegisterCardRequest));
    }
    
    @Test
    public void 카드반납() throws Exception {
        //given
         UserEntity savedUser = userRepository.save(UserEntity.builder()
                .deviceId("123")
                .nickname("KIM")
                .build());

        CardEntity savedStoredCard = cardRepository.save(CardEntity.builder()
                .cardInformation(new CardInformation(STORED_CARD_NUM))
                .initMoney(CardInitMoney.TEN)
                .build());

        UserRegisterCardRequest userRegisterCardRequest = new UserRegisterCardRequest(STORED_CARD_NUM, savedStoredCard.getCardInformation().getSerial());
        userCardService.registerCard(savedUser.getUserId(), userRegisterCardRequest);

        //when
        userCardService.returnCard(savedUser.getUserId());

        //then
        assertEquals(savedStoredCard.getCardStatus(), CardStatus.RETURNED_STATUS);
    }

    private String CARD_NUM = "1234123412341234";
    private CardInitMoney initMoney = CardInitMoney.TEN;

    @Test
    public void 카드등록확인() throws Exception {
        //given
        CardEntity cardEntity = CardEntity.builder()
                .cardInformation(new CardInformation(CARD_NUM))
                .initMoney(initMoney)
                .build();
        cardRepository.save(cardEntity);

        UserEntity userEntity = UserEntity.builder()
                .deviceId("TEST")
                .nickname("TEST")
                .build();
        userRepository.save(userEntity);

        userCardService.registerCard(userEntity.getUserId(), new UserRegisterCardRequest(CARD_NUM, cardEntity.getCardInformation().getSerial()));

        //when
        UserCardConfirmResponse userCardConfirmResponse = userCardService.getCardRegistrationStatus(userEntity.getUserId());

        //then
        assertEquals(userCardConfirmResponse.getOwnerId(), userEntity.getUserId());
        assertEquals(userCardConfirmResponse.getRemainAmount(), initMoney.getAmount().getValue());
    }
}