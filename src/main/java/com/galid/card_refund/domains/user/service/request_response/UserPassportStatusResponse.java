package com.galid.card_refund.domains.user.service.request_response;

import com.galid.card_refund.domains.user.domain.UserPassportStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPassportStatusResponse {
    private UserPassportStatus passportStatus;
}
