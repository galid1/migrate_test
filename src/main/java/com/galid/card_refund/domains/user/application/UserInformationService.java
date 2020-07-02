package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.common.aws.ImageType;
import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.application.request_response.UserInformationResponse;
import com.galid.card_refund.domains.user.application.request_response.UserInformationUpdateRequest;
import com.galid.card_refund.domains.user.application.request_response.UserPassportImageResponse;
import com.galid.card_refund.domains.user.application.request_response.UserPassportStatusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInformationService {
    private final UserRepository userRepository;
    private final S3FileUploader s3FileUploader;

    public UserInformationResponse getUserInformation(Long userId) {
        verifyExistUser(userId);
        UserEntity userEntity = userRepository.findById(userId).get();

        return UserInformationResponse.builder()
                .nickname(userEntity.getNickname())
                .passportImageUrl(userEntity.getPassPortImagePath())
                .build();
    }

    @Transactional
    public void updateUserInformation(Long userId, UserInformationUpdateRequest request) {
        verifyExistUser(userId);
        UserEntity userEntity = userRepository.findById(userId).get();

        userEntity.updateUserInformation(request.getNickname(),
                s3FileUploader.uploadFile(String.valueOf(userId), ImageType.PASSPORT_IMAGE, request.getUserPassportImageByte()));
    }

    public UserPassportStatusResponse getUserPassportStatus(Long userId) {
        verifyExistUser(userId);
        UserEntity userEntity = userRepository.findById((userId)).get();

        return new UserPassportStatusResponse(userEntity.getPassportStatus());
    }

    public UserPassportImageResponse getUserPassportImage(Long userId) {
        verifyExistUser(userId);
        UserEntity userEntity = userRepository.findById(userId).get();

        return new UserPassportImageResponse(userEntity.getPassPortImagePath());
    }

    private void verifyExistUser(Long userId) {
        if (!userRepository.findById(userId).isPresent())
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
    }
}
