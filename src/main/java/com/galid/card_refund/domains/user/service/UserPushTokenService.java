package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.PushTokenEntity;
import com.galid.card_refund.domains.user.domain.PushTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPushTokenService {
    private final PushTokenRepository pushTokenRepository;

    public void storeUserPushToken(long userId, String userPushToken) {
        PushTokenEntity pushTokenEntity = PushTokenEntity.builder()
                .pushToken(userPushToken)
                .userId(userId)
                .build();

        pushTokenRepository.save(pushTokenEntity);
    }

    public void updatePushToken(Long userId, String newPushToken) {
        PushTokenEntity findPushTokenEntity = findPushTokenEntityBy(userId);

        findPushTokenEntity.updatePushToken(newPushToken);
    }

    public String getPushToken(Long userId) {
        return findPushTokenEntityBy(userId).getPushToken();
    }

    private PushTokenEntity findPushTokenEntityBy(Long userId) {
        return pushTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }
}
