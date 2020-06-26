package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.card.domain.CardRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserCardConfirmResponse;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardRequest;
import com.galid.card_refund.domains.user.service.request_response.UserRegisterCardResponse;
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
    public UserRegisterCardResponse registerCard(long userId, UserRegisterCardRequest request) {
        UserEntity userEntity = getUserEntity(userId);
        CardEntity cardEntity = cardRepository.findByCardInformation_CardNum(request.getCardNum())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드번호입니다."));

        userEntity.registerCard(cardEntity);
        cardEntity.register(userId, request.getSerial());

        return new UserRegisterCardResponse(cardEntity.getCardId());
    }


    @Transactional
    public void returnCard(long userId) {
        UserEntity findUser = getUserEntity(userId);

        cardRepository.findById(findUser.getCard().getCardId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드입니다."))
                .returnCard();

        findUser.returnCard();
    }

    public UserCardConfirmResponse confirmCardRegistration(Long ownerId) {
        UserEntity userEntity = getUserEntity(ownerId);

        return toCardRegistrationConfirmResponse(userEntity.getCard());
    }

    private UserCardConfirmResponse toCardRegistrationConfirmResponse(CardEntity cardEntity) {
        return UserCardConfirmResponse.builder()
                .ownerId(cardEntity.getOwnerId())
                .remainAmount(cardEntity.getRemainAmount().getValue())
                .build();
    }

    private UserEntity getUserEntity(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }
}
