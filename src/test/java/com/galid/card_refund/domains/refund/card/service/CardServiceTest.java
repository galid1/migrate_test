package com.galid.card_refund.domains.refund.card.service;

import com.galid.card_refund.domains.refund.card.domain.CardInitMoney;
import com.galid.card_refund.domains.refund.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.card.domain.CardInformation;
import com.galid.card_refund.domains.refund.card.domain.CardRepository;
import com.galid.card_refund.domains.user.service.request_response.UserCardConfirmResponse;
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


}