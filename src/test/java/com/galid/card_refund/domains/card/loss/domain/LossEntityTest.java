package com.galid.card_refund.domains.card.loss.domain;

import com.galid.card_refund.domains.card.card.domain.CardEntity;
import com.galid.card_refund.domains.card.card.domain.CardInformation;
import com.galid.card_refund.domains.card.card.domain.CardInitMoney;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class LossEntityTest {
    @Autowired
    private LossRepository lossRepository;

    @Test
    public void LossEntity_생성() throws Exception {
        //given
        CardEntity cardEntity = new CardEntity(new CardInformation("1111222211112222"), CardInitMoney.TEN);
        LossEntity lossEntity = new LossEntity(cardEntity);

        //when
        Long lossId = lossRepository.save(lossEntity).getLossId();

        //then
        LossEntity foundEntity = lossRepository.findById(lossId).get();
        assertEquals(foundEntity, lossEntity);
        assertEquals(foundEntity.getLossStatus(), LossStatus.RECEIPT_STATUS);
        assertEquals(foundEntity.getLostCard(), cardEntity);
    }
}