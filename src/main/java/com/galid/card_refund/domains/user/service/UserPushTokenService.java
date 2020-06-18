package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.PushTokenEntity;
import com.galid.card_refund.domains.user.domain.PushTokenRepository;
import com.galid.card_refund.domains.user.service.request_response.GetPushTokenResponse;
import com.galid.card_refund.domains.user.service.request_response.StorePushTokenRequest;
import com.galid.card_refund.domains.user.service.request_response.UpdatePushTokenRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPushTokenService {
    private final PushTokenRepository pushTokenRepository;

    public void storeUserPushToken(long userId, StorePushTokenRequest request) {
        PushTokenEntity pushTokenEntity = PushTokenEntity.builder()
                .pushToken(request.getPushToken())
                .userId(userId)
                .build();

        pushTokenRepository.save(pushTokenEntity);
    }

    public void updatePushToken(Long userId, UpdatePushTokenRequest request) {
        PushTokenEntity findPushTokenEntity = findPushTokenEntityBy(userId);

        findPushTokenEntity.updatePushToken(request.getNewPushToken());
    }

    @Transactional(readOnly = true)
    public GetPushTokenResponse getPushToken(Long userId) {
        return new GetPushTokenResponse(findPushTokenEntityBy(userId).getPushToken());
    }

    private PushTokenEntity findPushTokenEntityBy(Long userId) {
        return pushTokenRepository.findFirstByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }
}
