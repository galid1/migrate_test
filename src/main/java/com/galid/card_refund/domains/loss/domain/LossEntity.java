package com.galid.card_refund.domains.loss.domain;

import com.galid.card_refund.common.logging.BaseAuditEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "loss")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LossEntity extends BaseAuditEntity {
    @Id @GeneratedValue
    private Long lossId;

    private Long lossCardId;
    @Enumerated(value = EnumType.STRING)
    private LossStatus lossStatus;

    @Builder
    public LossEntity(Long lossCardId) {
        this.lossCardId = lossCardId;
        this.lossStatus = LossStatus.RECEIPT_STATUS;
    }

    public void processLoss() {
        if(this.lossStatus != LossStatus.RECEIPT_STATUS)
            throw new IllegalStateException("이미 분실처리가 완료되었거나 취소된 상태입니다.");
        this.lossStatus = LossStatus.COMPLETE_STATUS;
    }

    public void cancel() {
        if(this.lossStatus == LossStatus.COMPLETE_STATUS)
            throw new IllegalStateException("이미 분실 처리가 완료된 경우 취소가 불가능합니다.");
        this.lossStatus = LossStatus.CANCEL_STATUS;
    }
}
