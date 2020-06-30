package com.galid.card_refund.domains.card.application.request_response;

import com.galid.card_refund.common.config.validation.CardNumLength;
import com.galid.card_refund.domains.card.domain.CardInitMoney;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardCreateRequest {
    @CardNumLength
    private String cardNum;

    private CardInitMoney cardInitMoney;
}
