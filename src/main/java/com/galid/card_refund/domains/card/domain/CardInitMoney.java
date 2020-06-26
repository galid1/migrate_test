package com.galid.card_refund.domains.card.domain;

import com.galid.card_refund.common.model.Money;

public enum CardInitMoney {
    TEN(100000), TWENTY(200000), THIRTY(300000), FIFTY(500000);

    Money money;

    CardInitMoney(int amount) {
        this.money = new Money(amount);
    }

    public Money getAmount() {
        return this.money;
    }
}
