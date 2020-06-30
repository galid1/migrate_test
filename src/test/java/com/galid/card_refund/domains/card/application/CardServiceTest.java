package com.galid.card_refund.domains.card.application;

import com.galid.card_refund.domains.card.domain.CardInitMoney;
import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.card.domain.CardRepository;
import com.galid.card_refund.domains.card.application.request_response.CardCreateRequest;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.application.UserCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class CardServiceTest {
    @Autowired
    private CardService cardService;

    @Autowired
    private UserCardService userCardService;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;

    private String CARD_NUM = "1234123412341234";
    private CardInitMoney initMoney = CardInitMoney.TEN;

    @Test
    public void 카드생성_테스트() throws Exception {
        //given
        CardCreateRequest request = new CardCreateRequest(CARD_NUM, CardInitMoney.TEN);
        //when
        Long createdCardId = cardService.createCard(request);

        //then
        CardEntity findEntity = cardRepository.findById(createdCardId).get();

        assertEquals(request.getCardNum(), findEntity.getCardInformation().getCardNum());
    }
}