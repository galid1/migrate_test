package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.refund.storedcard.domain.CardRegistration;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardEntity;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCardService {
    private final StoredCardRepository storedCardRepository;
    private final UserRepository userRepository;

    public void registerCard(long userId, UserRegisterCardRequest request) {
        StoredCardEntity storedCardEntity = storedCardRepository.findByCardInformation_CardNum(request.getCardNum())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드번호입니다."));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        storedCardEntity.register(toCardRegistration(userId, request));
        userEntity.registerCard(storedCardEntity.getStoredCardId());
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

        storedCardRepository.findById(findUser.getCardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드입니다."))
                .returnCard();

        findUser.returnCard();
    }
}
