package com.galid.card_refund.domains.user.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserPassportInformation {
    private String name;
    private String nation;
    private String passportNum;
    private String address;
}
