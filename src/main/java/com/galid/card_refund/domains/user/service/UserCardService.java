package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.card.domain.CardRegistration;
import com.galid.card_refund.domains.refund.card.domain.CardEntity;
import com.galid.card_refund.domains.refund.card.domain.CardRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserCardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registerCard(long userId, UserRegisterCardRequest request) {
        CardEntity cardEntity = cardRepository.findByCardInformation_CardNum(request.getCardNum())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드번호입니다."));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        cardEntity.register(toCardRegistration(userId, request));
        userEntity.registerCard(cardEntity);
    }

    private CardRegistration toCardRegistration(long userId, UserRegisterCardRequest request) {
        return CardRegistration.builder()
                .cardNum(request.getCardNum())
                .serial(request.getSerial())
                .userId(userId)
                .build();
    }

    public void returnCard(long userId) {
        UserEntity findUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        cardRepository.findById(findUser.getCard().getStoredCardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드입니다."))
                .returnCard();

        findUser.returnCard();
    }

    public Money checkRegistered(long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        return userEntity.getCard().getRemainAmount();
    }
}
