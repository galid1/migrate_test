package com.galid.card_refund.domains.refund.card.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import com.galid.card_refund.common.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CardEntity extends BaseEntity {
    @Id @GeneratedValue
    private long cardId;

    @Embedded
    private CardInformation cardInformation;

    @Enumerated(value = EnumType.STRING)
    private CardState cardState;

    private CardInitMoney initMoney;
    private LocalDate registeredDate;
    private Money remainAmount;

    private Long ownerId;

    @Builder
    public CardEntity(CardInformation cardInformation, CardInitMoney initMoney) {
        this.setCardInformation(cardInformation);
        this.registeredDate = LocalDate.now();
        this.setCardInitMoney(initMoney);
        this.remainAmount = initMoney.getAmount();
        cardState = CardState.UNREGISTERED;
    }

    private void setCardInformation(CardInformation cardInformation) {
        if(cardInformation == null)
            throw new IllegalArgumentException("Card 번호는 필수 입력 값입니다.");
        this.cardInformation = cardInformation;
    }

    private void setCardInitMoney(CardInitMoney initMoney) {
        if(initMoney == null)
            throw new IllegalArgumentException("카드의 초기금액은 필수 입력 값입니다.");
        this.initMoney = initMoney;
    }

    public void register(CardRegistration cardRegistration) {
        this.verifyRegistrableState();
        this.verifyRegistration(cardRegistration);

        this.ownerId = cardRegistration.getUserId();
        this.cardState = CardState.REGISTERED;
    }

    private void verifyRegistrableState() {
        if(this.cardState != CardState.UNREGISTERED)
            throw new IllegalStateException("이미 등록되었거나, 분실상태의 카드입니다.");
    }

    private void verifyRegistration(CardRegistration cardRegistration) {
        if(! cardRegistration.getSerial().equals(this.cardInformation.getSerial()))
            throw new IllegalArgumentException("serial 번호가 일치하지 않습니다.");
    }

    public void returnCard() {
        this.verifyRegistered();
        this.initCard();
    }

    public void initCard() {
        this.cardInformation.renewSerial();
        this.ownerId = null;
        this.registeredDate = LocalDate.now();
        this.remainAmount = initMoney.getAmount();
        this.cardState = CardState.UNREGISTERED;
    }

    public void recordRemainAmount(Money remainAmount) {
        verifyRegistered();
        this.remainAmount = remainAmount;
    }

    public void lostCard() {
        this.verifyRegistered();
        this.cardState = CardState.LOST;
    }

    private void verifyRegistered() {
        if(this.cardState != CardState.REGISTERED)
            throw new IllegalArgumentException("이미 반납 또는 분실처리된 카드입니다.");
    }
}
