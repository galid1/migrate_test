package com.galid.card_refund.domains.refund.storedcard.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import com.galid.card_refund.common.model.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stored_card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StoredCardEntity extends BaseEntity {
    @Id @GeneratedValue
    private long storedCardId;

    @Embedded
    private StoredCardInformation cardInformation;

    @Enumerated(value = EnumType.STRING)
    private StoredCardState cardState;

    private CardInitMoney initMoney;
    private LocalDate registeredDate;
    private Money remainAmount;

    private Long ownerId;

    @Builder
    public StoredCardEntity(StoredCardInformation cardInformation, CardInitMoney initMoney) {
        this.cardInformation = cardInformation;
        this.registeredDate = LocalDate.now();
        this.initMoney = initMoney;
        this.remainAmount = initMoney.getAmount();
        cardState = StoredCardState.UNREGISTERED;
    }

    public void register(CardRegistration cardRegistration) {
        this.verifyRegistrableState();
        this.verifyRegistration(cardRegistration);

        this.ownerId = cardRegistration.getUserId();
        this.cardState = StoredCardState.REGISTERED;
    }

    private void verifyRegistrableState() {
        if(this.cardState != StoredCardState.UNREGISTERED)
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
        this.cardState = StoredCardState.UNREGISTERED;
    }

    public void lostCard() {
        this.verifyRegistered();
        this.cardState = StoredCardState.LOST;
    }

    private void verifyRegistered() {
        if(this.cardState != StoredCardState.REGISTERED)
            throw new IllegalArgumentException("이미 반납 또는 분실처리된 카드입니다.");
    }
}
