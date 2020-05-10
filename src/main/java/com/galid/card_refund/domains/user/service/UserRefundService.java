package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.refund.domain.RefundLine;
import com.galid.card_refund.domains.refund.refund.domain.RefundRepository;
import com.galid.card_refund.domains.user.domain.UserEntity;
import com.galid.card_refund.domains.user.domain.UserPassportInformation;
import com.galid.card_refund.domains.user.domain.UserRepository;
import com.galid.card_refund.domains.user.exception.NotExistRefundRequestException;
import com.galid.card_refund.domains.user.service.request_response.UserRefundResultResponse;
import com.galid.card_refund.domains.user.service.request_response.UserRefundRequest;
import com.galid.card_refund.domains.user.service.request_response.UserRefundResponse;
import com.galid.card_refund.domains.user.service.request_response.UserRefundResultResponse.RefundResultResponseLine;
import com.galid.card_refund.domains.user.service.request_response.UserRefundResultResponse.UserInformationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRefundService {
    private final RefundRepository refundRepository;
    private final UserRepository userRepository;

    private final S3FileUploader s3FileUploader;
    private String IMAGE_PATH = "refund";

    @Transactional
    public UserRefundResponse refund(List<UserRefundRequest> refundLineList, Long requestorId) {
        verifyDuplicateRefundRequest(requestorId);

        RefundEntity refundEntity = RefundEntity.builder()
                .requestRefundLineList(toRefundLineList(requestorId, refundLineList))
                .requestorId(requestorId)
                .build();

        return new UserRefundResponse(refundRepository.save(refundEntity)
                .getExpectRefundAmount()
                .getValue());
    }

    private void verifyDuplicateRefundRequest(Long requestorId) {
        if(refundRepository.findByRequestorId(requestorId).isPresent())
            throw new IllegalArgumentException("환급 요청은 한번만 가능합니다.");
    }

    private List<RefundLine> toRefundLineList(Long requestorId, List<UserRefundRequest> refundRequestList) {
        return refundRequestList.stream()
                .map(userRefundRequest ->
                        RefundLine.builder()
                                .place(userRefundRequest.getPlace())
                                .paymentAmount(new Money(userRefundRequest.getPaymentAmount()))
                                .itemImageUrl(s3FileUploader.uploadFile(makeS3UploadPath(requestorId),
                                        Base64.getDecoder().decode(userRefundRequest.getBase64RefundImage())))
                                .purchaseDateTime(userRefundRequest.getPurchaseDateTime())
                                .build()
                )
                .collect(Collectors.toList());
    }

    private String makeS3UploadPath(Long requestorId) {
        return IMAGE_PATH + "/" + requestorId;
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
                .build();
    }

    private UserInformationDto toUserInformationDto(UserEntity userEntity) {
        UserPassportInformation userPassportInformation = userEntity.getUserPassportInformation();
        if(userPassportInformation == null) {
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
