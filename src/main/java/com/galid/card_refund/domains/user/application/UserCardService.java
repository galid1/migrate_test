package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.card.domain.CardRepository;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.application.request_response.UserCardConfirmResponse;
import com.galid.card_refund.domains.user.application.request_response.UserRegisterCardRequest;
import com.galid.card_refund.domains.user.application.request_response.UserRegisterCardResponse;
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
        verifyExistUser(userId);
        CardEntity cardEntity = cardRepository.findByCardInformation_CardNum(request.getCardNum())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드번호입니다."));

        cardEntity.register(userId, request.getSerial());

        return new UserRegisterCardResponse(cardEntity.getCardId());
    }


    @Transactional
    public void returnCard(long ownerId) {
        findCardByOwnerId(ownerId).returnCard();
    }

    public UserCardConfirmResponse getCardRegistrationStatus(Long ownerId) {
        return toCardRegistrationConfirmResponse(findCardByOwnerId(ownerId));
    }

    private void verifyExistUser(Long userId) {
        if(!userRepository.findById(userId).isPresent())
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
    }

    private CardEntity findCardByOwnerId(Long ownerId) {
        return cardRepository.findFirstByOwnerIdOrderByCreatedDateDesc(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 소유한 카드가 존재하지 않습니다."));
    }

    private UserCardConfirmResponse toCardRegistrationConfirmResponse(CardEntity cardEntity) {
        return UserCardConfirmResponse.builder()
                .ownerId(cardEntity.getOwnerId())
                .remainAmount(cardEntity.getRemainAmount().getValue())
                .build();
    }
}
