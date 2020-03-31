package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardEntity;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardInformation;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardRepository;
import com.galid.card_refund.domains.refund.usercard.domain.UserCardEntity;
import com.galid.card_refund.domains.refund.usercard.domain.UserCardRepository;
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
public class UserRegisterCardServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoredCardRepository storedCardRepository;
    @Autowired
    private UserCardRepository userCardRepository;

    @Autowired
    private UserRegisterCardService userRegisterCardService;

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

        StoredCardEntity savedStored = storedCardRepository.save(StoredCardEntity.builder()
                .cardInformation(StoredCardInformation.builder()
                        .cardNum(STORED_CARD_NUM)
                        .build())
                .build());

        UserRegisterCardRequest userRegisterCardRequest = new UserRegisterCardRequest(STORED_CARD_NUM, savedStored.getCardInformation().getSerial());


        //when
        userRegisterCardService.registerCard(savedUser.getUserId(), userRegisterCardRequest);


        //then
        UserCardEntity findUserCardEntity = userCardRepository.findById(savedUser.getUserCardId()).get();
        assertEquals(findUserCardEntity.getUserCardInformation().getCardNum(), userRegisterCardRequest.getCardNum());
        assertEquals(findUserCardEntity.getUserCardInformation().getSerial(), userRegisterCardRequest.getSerial());
    }

    @Test
    public void 존재하지_않는사용자_카드등록_예외() throws Exception {
        //given
        StoredCardEntity savedStoredCard = storedCardRepository.save(StoredCardEntity.builder()
                .cardInformation(StoredCardInformation
                        .builder()
                        .cardNum(STORED_CARD_NUM)
                        .build())
                .build());

        UserRegisterCardRequest userRegisterCardRequest = new UserRegisterCardRequest(STORED_CARD_NUM, savedStoredCard.getCardInformation().getSerial());

        //when, then
        long notExistUserId = 1l;
        assertThrows(IllegalArgumentException.class, () -> userRegisterCardService.registerCard(notExistUserId, userRegisterCardRequest));
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
        assertThrows(IllegalArgumentException.class, () -> userRegisterCardService.registerCard(savedUserId, userRegisterCardRequest));
    }
}