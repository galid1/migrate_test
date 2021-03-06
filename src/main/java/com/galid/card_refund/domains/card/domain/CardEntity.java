package com.galid.card_refund.domains.card.domain;

import com.galid.card_refund.common.logging.BaseAuditEntity;
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
public class CardEntity extends BaseAuditEntity {
    @Id @GeneratedValue
    private long cardId;

    private CardInitMoney initMoney;
    private LocalDate registeredDate;
    private Money remainAmount;
    @Enumerated(value = EnumType.STRING)
    private CardStatus cardStatus;

    @Embedded
    private CardInformation cardInformation;

    private Long ownerId;

    @Builder
    public CardEntity(CardInformation cardInformation, CardInitMoney initMoney) {
        this.setCardInformation(cardInformation);
        this.registeredDate = LocalDate.now();
        this.setCardInitMoney(initMoney);
        this.remainAmount = initMoney.getAmount();
        cardStatus = CardStatus.UNREGISTERED_STATUS;
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

    public void register(Long ownerId, String serial) {
        this.verifyRegistrableState();
        this.verifySerialNumber(serial);

        this.ownerId = ownerId;
        this.cardStatus = CardStatus.REGISTERED_STATUS;
    }

    private void verifyRegistrableState() {
        if(this.cardStatus != CardStatus.UNREGISTERED_STATUS)
            throw new IllegalStateException("이미 등록되었거나, 분실상태의 카드입니다.");
    }

    private void verifySerialNumber(String serial) {
        if(! serial.equals(this.cardInformation.getSerial()))
            throw new IllegalArgumentException("serial 번호가 일치하지 않습니다.");
    }

    public void returnCard() {
        this.verifyRegistered();
        this.cardStatus = CardStatus.RETURNED_STATUS;
    }

    public void recordRemainAmount(Money remainAmount) {
        //TODO 카드 상태에 따라 기능을 제한하고 싶은 경우 주석 해제
//        verifyRegistered();
        this.remainAmount = remainAmount;
    }

    public void reportLoss() {
        //TODO 카드 상태에 따라 기능을 제한하고 싶은 경우 주석 해제
//        this.verifyRegistered();
        this.cardStatus = CardStatus.LOSS_STATUS;
    }

    private void verifyRegistered() {
        if(this.cardStatus != CardStatus.REGISTERED_STATUS)
            throw new IllegalArgumentException("이미 반납 또는 분실처리된 카드입니다.");
    }
}
