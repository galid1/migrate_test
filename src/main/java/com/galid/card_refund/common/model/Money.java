package com.galid.card_refund.common.model;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Money {
    private double value;

    public Money multiply(int multiply) {
        return Money.builder()
                .value(this.value * multiply)
                .build();
    }
}
