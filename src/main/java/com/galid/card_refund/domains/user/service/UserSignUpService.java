package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpRequest;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserSignUpService {
    private final UserRepository userRepository;
    private final S3FileUploader s3FileUploader;

    private String IMAGE_PATH_KEY = "user";

    @Transactional
    public UserSignUpResponse signUp(UserSignUpRequest request) throws IOException {
        validateDuplicateUser(request.getDeviceId());

//        String passPortImagePath = s3FileUploader.uploadFile(IMAGE_PATH_KEY, Base64.decodeBase64(request.getBase64PassPortImage()));
        String passPortImagePath = s3FileUploader.uploadFile(IMAGE_PATH_KEY, request.getBase64PassPortImage().getBytes());

        UserEntity newUser = UserEntity.builder()
                .deviceId(request.getDeviceId())
                .nickname(request.getNickname())
                .passPortImagePath(passPortImagePath)
                .build();

        return new UserSignUpResponse(this.userRepository.save(newUser).getUserId());
    }

    private void validateDuplicateUser(String deviceId) {
        if(userRepository.findByDeviceId(deviceId).isPresent())
            throw new IllegalArgumentException("이미 가입된 디바이스 ID 입니다.");
    }

}
