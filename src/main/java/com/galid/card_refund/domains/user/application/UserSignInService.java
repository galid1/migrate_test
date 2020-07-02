package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.common.jwt.JwtUtil;
import com.galid.card_refund.domains.user.domain.*;
import com.galid.card_refund.domains.user.domain.ApiTokenEntity;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.application.request_response.UserSignInRequest;
import com.galid.card_refund.domains.user.application.request_response.UserSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSignInService {
    private final UserRepository userRepository;
    private final ApiTokenRepository apiTokenRepository;
    private final JwtUtil jwtUtil;

    public UserSignInResponse signIn(UserSignInRequest request) {
        UserEntity userEntity = userRepository.findFirstByDeviceId(request.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 유저입니다."));

        String token = generateToken(userEntity.getUserId());

        return new UserSignInResponse(userEntity.getUserId(), token);
    }

    private String generateToken(Long userId) {
        Optional<ApiTokenEntity> findToken = apiTokenRepository.findFirstByUserId(userId);

        if(findToken.isPresent()) {
            return findToken.get()
                    .getApiToken();
        }
        else {
            String token = jwtUtil.generateToken();
            ApiTokenEntity apiTokenEntity = ApiTokenEntity.builder()
                    .apiToken(token)
                    .userId(userId)
                    .build();
            apiTokenRepository.save(apiTokenEntity);

            return token;
        }
    }

}
