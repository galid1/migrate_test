package com.galid.card_refund.domains.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.galid.card_refund.common.model.Money;
import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsageHistory {
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm", timezone="Asia/Seoul")
    private LocalDateTime date;
    private String place;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "payment_amount"))
    private Money paymentAmount;
    @AttributeOverride(name = "value", column = @Column(name = "remain_amount"))
    @Embedded
    private Money remainAmount;
}