package com.galid.card_refund.domains.card.application.request_response;

import com.galid.card_refund.common.spring_config.validation.CardNumLength;
import com.galid.card_refund.domains.card.domain.CardInitMoney;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardCreateRequest {
    @CardNumLength
    private String cardNum;

    @NotNull
    private CardInitMoney cardInitMoney;
}
