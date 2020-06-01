package com.galid.card_refund.domains.user.service;

import com.galid.card_refund.common.aws.ImageType;
import com.galid.card_refund.common.aws.S3FileUploader;
import com.galid.card_refund.common.model.Money;
import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.domain.RefundLine;
import com.galid.card_refund.domains.refund.domain.RefundRepository;
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

import java.util.List;

import java.util.Map;
import static java.util.Map.entry;

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
    public UserRefundResponse refund(Long requestorId, List<UserRefundRequest> refundLineList, Map<String, byte[]> refundItemImageByteMap) {
        verifyDuplicateRefundRequest(requestorId);
        verifyRefundRequestImageCount(refundLineList, refundItemImageByteMap);

        Map<String, String> refundItemImageUrlMap = uploadRefundItemImageList(requestorId, refundItemImageByteMap);

        RefundEntity refundEntity = RefundEntity.builder()
                .requestRefundLineList(toRefundLineList(refundLineList, refundItemImageUrlMap))
                .requestorId(requestorId)
                .build();

        RefundEntity savedRefundEntity = refundRepository.save(refundEntity);

        return new UserRefundResponse(savedRefundEntity.getRefundId(),
                savedRefundEntity.getExpectRefundAmount().getValue());
    }

    private void verifyDuplicateRefundRequest(Long requestorId) {
        if (refundRepository.findByRequestorId(requestorId).isPresent())
            throw new IllegalArgumentException("환급 요청은 한번만 가능합니다.");
    }

    private void verifyRefundRequestImageCount(List<UserRefundRequest> refundLineList, Map<String, byte[]> refundItemImageByteMap) {
        if (refundLineList.size() != refundItemImageByteMap.size())
            throw new IllegalArgumentException("환급 요청 상품의 수와 상품의 이미지 수가 다릅니다.");
    }

    private Map<String, String> uploadRefundItemImageList(Long requestorId, Map<String, byte[]> refundItemImageByteMap) {
        return refundItemImageByteMap.entrySet()
                .stream()
                .map(e -> entry(e.getKey(),
                                s3FileUploader.uploadFile(String.valueOf(requestorId), ImageType.REFUND_IMAGE, e.getValue())
                        )
                )
                .collect(toMap(e -> e.getKey(), e -> e.getValue()));
    }

    private List<RefundLine> toRefundLineList(List<UserRefundRequest> refundRequestList,
                                              Map<String, String> refundItemImageUrlMap) {
        return refundRequestList.stream()
                .map(userRefundRequest ->
                        RefundLine.builder()
                                .place(userRefundRequest.getPlace())
                                .itemImageUrl(refundItemImageUrlMap.get(userRefundRequest.getRefundItemId()))
                                .paymentAmount(new Money(userRefundRequest.getPaymentAmount()))
                                .purchaseDateTime(userRefundRequest.getPurchaseDateTime())
                                .build()
                )
                .collect(Collectors.toList());
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
