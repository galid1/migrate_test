package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.aws.ImageType;
import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpRequest;
import com.galid.card_refund.domains.user.service.request_response.UserSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class UserSignUpService {
    private final UserRepository userRepository;
    private final S3FileUploader s3FileUploader;

    @Transactional
    public UserSignUpResponse signUp(UserSignUpRequest request, MultipartFile passportImage) throws IOException {
        validateDuplicateUser(request.getDeviceId());

        UserEntity newUser = UserEntity.builder()
                .deviceId(request.getDeviceId())
                .nickname(request.getNickname())
                .build();

        UserEntity savedUser = this.userRepository.save(newUser);
        uploadUserPassportImage(passportImage, savedUser);

        return new UserSignUpResponse(savedUser.getUserId());
    }

    private void validateDuplicateUser(String deviceId) {
        if(userRepository.findByDeviceId(deviceId).isPresent())
            throw new IllegalArgumentException("이미 가입된 디바이스 ID 입니다.");
    }

    private void uploadUserPassportImage(MultipartFile passportImage, UserEntity newUser) throws IOException {
        String passPortImagePath = s3FileUploader.uploadFile(String.valueOf(newUser.getUserId()),
                ImageType.PASSPORT_IMAGE,
                passportImage.getBytes());

        newUser.changePassportImage(passPortImagePath);
    }
}
