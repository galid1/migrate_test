package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.common.aws.ImageType;
import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.application.request_response.UserSignUpRequest;
import com.galid.card_refund.domains.user.application.request_response.UserSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserSignUpService {
    private final UserRepository userRepository;
    private final S3FileUploader s3FileUploader;

    @Transactional
    public UserSignUpResponse signUp(UserSignUpRequest request) {
        validateDuplicateUser(request.getDeviceId());

        UserEntity newUser = UserEntity.builder()
                .deviceId(request.getDeviceId())
                .nickname(request.getNickname())
                .build();

        UserEntity savedUser = this.userRepository.save(newUser);
        uploadUserPassportImage(request.getPassportImageByte(), savedUser);

        return new UserSignUpResponse(savedUser.getUserId());
    }

    private void validateDuplicateUser(String deviceId) {
        if(userRepository.findFirstByDeviceId(deviceId).isPresent())
            throw new IllegalArgumentException("이미 가입된 디바이스 ID 입니다.");
    }

    private void uploadUserPassportImage(byte[] passportImageByte, UserEntity newUser) {
        String passPortImagePath = s3FileUploader.uploadFile(String.valueOf(newUser.getUserId()),
                ImageType.PASSPORT_IMAGE,
                passportImageByte);

        newUser.uploadPassportImagePath(passPortImagePath);
    }
}
