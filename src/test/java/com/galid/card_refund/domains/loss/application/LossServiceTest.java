package com.galid.card_refund.domains.loss.application;

import com.galid.card_refund.common.BaseTestConfig;
import com.galid.card_refund.config.CardSetUp;
import com.galid.card_refund.config.UserSetUp;
import com.galid.card_refund.domains.card.domain.CardEntity;
import com.galid.card_refund.domains.loss.domain.LossEntity;
import com.galid.card_refund.domains.loss.domain.LossRepository;
import com.galid.card_refund.domains.loss.domain.LossStatus;
import com.galid.card_refund.domains.user.domain.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LossServiceTest extends BaseTestConfig {
    @Autowired
    private UserSetUp userSetUp;
    @Autowired
    private CardSetUp cardSetUp;
    @Autowired
    private LossRepository lossRepository;
    @Autowired
    private LossService lossService;

    private UserEntity TEST_USER_ENTITY;
    private CardEntity TEST_CARD_ENTITY;
    private Long TEST_USER_ID;

    @BeforeEach
    public void init() {
        TEST_USER_ENTITY = userSetUp.saveUser();
        TEST_USER_ID = TEST_USER_ENTITY.getUserId();
        TEST_CARD_ENTITY = cardSetUp.saveCard();
        userSetUp.registerCard(TEST_USER_ENTITY, TEST_CARD_ENTITY);
    }

    @Test
    public void 분실신고() throws Exception {
        //given
        Long lossId = lossService.reportLossCard(TEST_USER_ID);

        //when
        LossEntity lossEntity = lossRepository.findById(lossId).get();

        //then
        assertThat(lossId, is(equalTo(lossEntity.getLossId())));
        assertThat(lossEntity.getLossStatus(), is(equalTo(LossStatus.RECEIPT_STATUS)));
    }

    @Test
    public void 분실신고_중복시_에러() throws Exception {
        //given
        lossService.reportLossCard(TEST_USER_ID);

        //when, then
        assertThrows(
                IllegalStateException.class,
                () -> lossService.reportLossCard(TEST_USER_ID)
        );
    }

    @Test
    public void 분실신고_처리() throws Exception {
        //given
        Long lossId = lossService.reportLossCard(TEST_USER_ID);
        LossEntity lossEntity = lossRepository.findById(lossId).get();

        //when
        lossService.processLoss(TEST_USER_ID);

        //then
        assertThat(lossEntity.getLossStatus(), is(equalTo(LossStatus.COMPLETE_STATUS)));
    }

    /**
     * LossEntity 의 IllegalStateException 에 걸릴것 같지만, LossService 에서, 현재 접수된 분실신고 내역을,
     * 분실신고 상태를 기반으로 검색하여 가져오기 때문에, lossEntity.cancel()을 호출하기전,
     * LossEntity를 검색하는 중에 에러가 발생한다.
     */
    @Test
    public void 분실신고_처리시_취소불가() throws Exception {
        //given
        lossService.reportLossCard(TEST_USER_ID);

        //when
        lossService.processLoss(TEST_USER_ID);

        //then
        assertThrows(
                IllegalArgumentException.class,
                () -> lossService.cancelLoss(TEST_USER_ID)
        );
    }

    @Test
    public void 분실신고_취소시_처리불가() throws Exception {
        //given
        lossService.reportLossCard(TEST_USER_ID);

        //when
        lossService.cancelLoss(TEST_USER_ID);

        //then
        assertThrows(
                IllegalArgumentException.class,
                () -> lossService.processLoss(TEST_USER_ID)
        );
    }
}