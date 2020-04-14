package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserSignInRequest;
import com.galid.card_refund.domains.user.service.request_response.UserSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignInService {
    private final UserRepository userRepository;

    public UserSignInResponse signIn(UserSignInRequest request) {
        UserEntity userEntity = userRepository.findByDeviceId(request.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 유저입니다."));

        return new UserSignInResponse(userEntity.getUserId());
    }
}
