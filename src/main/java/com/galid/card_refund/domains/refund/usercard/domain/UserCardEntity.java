package com.galid.card_refund.domains.refund.usercard.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCardEntity extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "user_card_id")
    private long userCardId;

    @Embedded
    private UserCardInformation userCardInformation;

    @Enumerated(EnumType.STRING)
    private UserCardState cardState;

    @ElementCollection
    @CollectionTable(name = "usage_history",
    joinColumns = @JoinColumn(name = "user_card_id"))
    @Getter(AccessLevel.PRIVATE)
    private List<UsageHistory> usageHistoryList;

    @Builder
    public UserCardEntity(UserCardInformation userCardInformation) {
        this.userCardInformation = userCardInformation;
        this.cardState = UserCardState.REGISTERED;
        this.usageHistoryList = new ArrayList<>();
    }

    public void recordCardUsage(UsageHistory usageHistory) {
        verifyIsRegistered();

        this.usageHistoryList.add(usageHistory);
    }

    public List<UsageHistory> getUsageHistory() {
        verifyIsRegistered();
        return this.usageHistoryList;
    }

    public void returnCard() {
        verifyIsRegistered();

        this.cardState = UserCardState.RETURNED;
    }

    public void lostCard() {
        verifyIsRegistered();

        this.cardState = UserCardState.LOST;
    }

    private void verifyIsRegistered() {
        if(this.cardState != UserCardState.REGISTERED)
            throw new IllegalStateException("이미 반납 또는 분실처리된 카드입니다.");
    }
}
