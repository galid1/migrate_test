package com.galid.card_refund.common.pushnotification;

import com.galid.card_refund.domains.refund.domain.RefundEntity;
import com.galid.card_refund.domains.refund.domain.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class FcmAopConfig {
    private final FirebaseCloudMessageService messageService;
    private final RefundRepository refundRepository;

    @After("execution(* com.galid.card_refund.domains.admin.application.AdminEstimateUserPassportService.estimateUserPassport(..))")
    public void sendFcmMessageWhenEstimatePassport(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        messageService.sendMessageTo((Long)args[0], "Tour Cash", "여권정보 평가 완료");
    }

    @After("execution(* com.galid.card_refund.domains.admin.application.AdminEstimateRefundService.estimateRefundRequest(..))")
    public void sendFcmMessageWhenEstimateRefund(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        RefundEntity refundEntity = refundRepository.findById((Long) args[0]).get();

        messageService.sendMessageTo(refundEntity.getRequestorId(), "Tour Cash", "환급 요청 평가 완료");
    }
}
