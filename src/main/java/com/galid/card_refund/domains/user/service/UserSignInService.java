package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.jwt.JwtUtil;
import com.galid.card_refund.domains.user.domain.TokenEntity;
import com.galid.card_refund.domains.user.domain.TokenRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.infra.TokenQuery;
import com.galid.card_refund.domains.user.service.request_response.UserSignInRequest;
import com.galid.card_refund.domains.user.service.request_response.UserSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSignInService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final TokenQuery tokenQuery;

    public UserSignInResponse signIn(UserSignInRequest request) {
        UserEntity userEntity = userRepository.findByDeviceId(request.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 유저입니다."));

        String token = generateToken(userEntity);

        return new UserSignInResponse(userEntity.getUserId(), token);
    }

    private String generateToken(UserEntity user) {
        Optional<TokenEntity> findToken = tokenQuery.getTokenBy(user.getUserId());

        if(findToken.isPresent()) {
            return findToken.get()
                    .getToken();
        }
        else {
            String token = jwtUtil.generateToken();
            TokenEntity tokenEntity = TokenEntity.builder()
                    .token(token)
                    .user(user)
                    .build();
            tokenRepository.save(tokenEntity);

            return token;
        }
    }

}
