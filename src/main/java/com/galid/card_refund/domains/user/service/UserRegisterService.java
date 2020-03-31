package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegisterService {
    private UserRepository userRepository;

    public UserRegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public long registerUser(UserRegisterRequest request) {
        UserEntity newUser = UserEntity.builder()
                .deviceId(request.getDeviceId())
                .nickname(request.getNickname())
                .build();

        validateDuplicateUser(newUser);

        return this.userRepository.save(newUser).getUserId();
    }

    private void validateDuplicateUser(UserEntity userEntity) {
        if(userRepository.findByDeviceId(userEntity.getDeviceId()).isPresent())
            throw new IllegalArgumentException("이미 가입된 디바이스 ID 입니다.");
    }
}
