package com.galid.card_refund.domains.user.application;

import com.galid.card_refund.common.aws.ImageType;
import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.domain.RefundLine;
import com.galid.card_refund.domains.refund.domain.RefundRepository;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequestList;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserPassportInformation;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.exception.NotExistRefundRequestException;
import com.galid.card_refund.domains.user.application.request_response.UserRefundResultResponse;
import com.galid.card_refund.domains.user.application.request_response.UserRefundRequest;
import com.galid.card_refund.domains.user.application.request_response.UserRefundResponse;
import com.galid.card_refund.domains.user.application.request_response.UserRefundResultResponse.RefundResultResponseLine;
import com.galid.card_refund.domains.user.application.request_response.UserRefundResultResponse.UserInformationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRefundService {
    private final RefundRepository refundRepository;
    private final UserRepository userRepository;

    private final S3FileUploader s3FileUploader;

    @Transactional
    public UserRefundResponse refund(Long requestorId, UserRefundRequestList request) {
        verifyDuplicateRefundRequest(requestorId);

        RefundEntity refundEntity = RefundEntity.builder()
                .requestRefundLineList(toRefundLineList(String.valueOf(requestorId), request.getUserRefundRequestList()))
                .requestorId(requestorId)
                .build();

        RefundEntity savedRefundEntity = refundRepository.save(refundEntity);

        return new UserRefundResponse(savedRefundEntity.getRefundId(),
                savedRefundEntity.getExpectRefundAmount().getValue());
    }

    private List<RefundLine> toRefundLineList(String imageKey, List<UserRefundRequest> userRefundRequestList) {
        return userRefundRequestList.stream()
                .map(request -> RefundLine.builder()
                        .itemImageUrl(s3FileUploader.uploadFile(imageKey, ImageType.REFUND_IMAGE, request.getRefundItemImageByte()))
                        .paymentAmount(new Money(request.getPaymentAmount()))
                        .place(request.getPlace())
                        .purchaseDateTime(request.getPurchaseDateTime())
                        .build())
                .collect(Collectors.toList());
    }

    private void verifyDuplicateRefundRequest(Long requestorId) {
        if (refundRepository.findByRequestorId(requestorId).isPresent())
            throw new IllegalArgumentException("환급 요청은 한번만 가능합니다.");
    }

    public UserRefundResultResponse getRefundRequestResult(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        RefundEntity refundEntity = refundRepository.findByRequestorId(userId)
                .orElseThrow(() -> new NotExistRefundRequestException());

        return UserRefundResultResponse.builder()
                .refundResultResponseLineList(refundEntity.getRefundResultLineList().stream()
                        .map(r -> RefundResultResponseLine.builder()
                                .paymentAmount(r.getPaymentAmount())
                                .refundAmount(r.getRefundAmount())
                                .place(r.getPlace())
                                .build())
                        .collect(Collectors.toList()))
                .userInformation(toUserInformationDto(userEntity))
                .unRefundableLineDescription(refundEntity.getUnRefundableLineDescription())
                .refundResultBarcodeImageUrl(refundEntity.getRefundResultBarcodeImageUrl())
                .build();
    }

    private UserInformationDto toUserInformationDto(UserEntity userEntity) {
        UserPassportInformation userPassportInformation = userEntity.getUserPassportInformation();
        if (userPassportInformation == null) {
            throw new IllegalArgumentException("여권정보가 아직 입력되지 않은 상태입니다.");
        }

        return UserRefundResultResponse.UserInformationDto.builder()
                .address(userPassportInformation.getAddress())
                .name(userPassportInformation.getName())
                .nation(userPassportInformation.getNation())
                .passportNum(userPassportInformation.getPassportNum())
                .build();
    }
}
