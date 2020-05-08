package com.galid.card_refund.domains.user.service.request_response;

import com.galid.card_refund.domains.user.domain.UserPassportState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPassportStateResponse {
    private UserPassportState passportState;
}
