package com.galid.card_refund.config;

import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSetUp {
    @Autowired
    private UserRepository userRepository;

    public UserEntity saveUser(String testDeviceId, String testNickname, String testPassportImageUrl) {
        UserEntity savedUser = userRepository.save(UserEntity.builder()
                .deviceId(testDeviceId)
                .nickname(testNickname)
                .build());
        savedUser.uploadPassportImagePath(testPassportImageUrl);

        return savedUser;
    }
}
