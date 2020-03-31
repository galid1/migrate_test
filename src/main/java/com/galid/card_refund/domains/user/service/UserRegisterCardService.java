package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.refund.storedcard.domain.CardRegistration;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardEntity;
import com.galid.card_refund.domains.refund.storedcard.domain.StoredCardRepository;
import com.galid.card_refund.domains.refund.usercard.domain.UserCardEntity;
import com.galid.card_refund.domains.refund.usercard.domain.UserCardInformation;
import com.galid.card_refund.domains.refund.usercard.domain.UserCardRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRegisterCardService {
    private final StoredCardRepository storedCardRepository;
    private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;

    public void registerCard(long userId, UserRegisterCardRequest request) {
        StoredCardEntity storedCardEntity = storedCardRepository.findByCardInformation_CardNum(request.getCardNum())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드번호입니다."));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        long userCardId = createUserCard(request);
        userEntity.registerCard(userCardId);
        storedCardEntity.register(toCardRegistration(userId, userCardId, request));
    }

    private CardRegistration toCardRegistration(long userId, long userCardId, UserRegisterCardRequest request) {
        return CardRegistration.builder()
                .cardNum(request.getCardNum())
                .serial(request.getSerial())
                .userId(userId)
                .userCardId(userCardId)
                .build();
    }

    private long createUserCard(UserRegisterCardRequest request) {
        UserCardEntity cardEntity = UserCardEntity.builder()
                .userCardInformation(UserCardInformation.builder()
                        .cardNum(request.getCardNum())
                        .serial(request.getSerial())
                        .build())
                .build();

        return userCardRepository.save(cardEntity)
                .getUserCardId();
    }
}
