package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.refund.card.domain.*;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserCardServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserCardService userCardService;

    private String STORED_CARD_NUM = "1234123412341234";

    /**
     * 존재하지 않는 사용자에게 등록할 때 예외 (미리 등록된 유저 필요)
     * 존재하지 않는 카드번호를 이용해 등록할 때 예외 (미리 등록된 카드 필요)
     * */
    @Test
    public void 카드등록() throws Exception {
        //given
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .deviceId("123")
                .nickname("KIM")
                .build());

        CardEntity savedStoredCard = cardRepository.save(CardEntity.builder()
                .cardInformation(CardInformation.builder()
                        .cardNum(STORED_CARD_NUM)
                        .build())
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
                .cardInformation(CardInformation
                        .builder()
                        .cardNum(STORED_CARD_NUM)
                        .build())
                .initMoney(CardInitMoney.TEN)
                .build();

        cardRepository.save(savedStoredCard);

        UserRegisterCardRequest userRegisterCardRequest = new UserRegisterCardRequest(STORED_CARD_NUM, savedStoredCard.getCardInformation().getSerial());

        //when, then
        long notExistUserId = 1l;
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
        Long savedUserId = savedUser.getUserId();

        CardEntity savedStoredCard = cardRepository.save(CardEntity.builder()
                .cardInformation(CardInformation.builder()
                        .cardNum(STORED_CARD_NUM)
                        .build())
                .initMoney(CardInitMoney.TEN)
                .build());

        UserRegisterCardRequest userRegisterCardRequest = new UserRegisterCardRequest(STORED_CARD_NUM, savedStoredCard.getCardInformation().getSerial());
        userCardService.registerCard(savedUserId, userRegisterCardRequest);

        //when
        userCardService.returnCard(savedUserId);

        //then
        assertEquals(savedUser.getCard(), null);
        assertEquals(savedStoredCard.getOwnerId(), null);
        assertEquals(savedStoredCard.getCardState(), CardState.UNREGISTERED);
    }
}