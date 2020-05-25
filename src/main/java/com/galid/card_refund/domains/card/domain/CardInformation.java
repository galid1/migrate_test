package com.galid.card_refund.domains.card.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Random;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardInformation {
    private String serial;
    @Column(unique = true)
    private String cardNum;

    @Transient
    private int MAX_SERIAL_NUM = 9999;
    @Transient
    private int MIN_SERIAL_NUM = 1;

    public CardInformation(String cardNum) {
        this.setCardNum(cardNum);
        this.serial = this.makeFourDigitNewSerial();
    }

    private void setCardNum(String cardNum) {
        verifyCardNumLength(cardNum);
        this.cardNum = cardNum;
    }

    private void verifyCardNumLength(String cardNum) {
        if(cardNum.length() != 16)
            throw new IllegalArgumentException("카드번호는 16자리이어야 합니다.");
    }

    public void renewSerial() {
        String newSerial = makeFourDigitNewSerial();;

        while(this.serial.equals(newSerial)) {
            newSerial = makeFourDigitNewSerial();
        }

        this.serial = newSerial;
    }

    private String makeFourDigitNewSerial() {
        int lestThanAThousandNum = new Random().nextInt(MAX_SERIAL_NUM - MIN_SERIAL_NUM + 1) - MIN_SERIAL_NUM;
        String lestThanAThousandString = String.valueOf(lestThanAThousandNum);

        for(int i = 0; i < 3; i ++) {
            if(lestThanAThousandString.length() == 4)
                return lestThanAThousandString;

            lestThanAThousandString = "0" + lestThanAThousandString;
        }

        return lestThanAThousandString;
    }
}
