package com.galid.card_refund.domains.user.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {
    @Id @GeneratedValue
    private Long userId;

    private String deviceId;
    private String nickname;
    private String passPortImagePath;

    private Long cardId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_card_usage_history",
            joinColumns = @JoinColumn(name = "user_id"))
    private List<UsageHistory> usageHistoryList = new ArrayList<>();

    @Builder
    public UserEntity(String deviceId, String nickname, String passPortImagePath) {
        this.setDeviceId(deviceId);
        this.setNickname(nickname);
        this.passPortImagePath = passPortImagePath;
    }

    private void setDeviceId(String deviceId) {
        if (deviceId == null || deviceId.length() < 1)
            throw new IllegalArgumentException("Device ID를 입력하세요.");

        this.deviceId = deviceId;
    }

    private void setNickname(String nickname) {
        if(nickname == null || nickname.length() < 3)
            throw new IllegalArgumentException("Nickname은 3글자 이상이어야합니다.");
        this.nickname = nickname;
    }

    public void registerCard(long cardId) {
        if(this.cardId != null)
            throw new IllegalStateException("이미 등록된 카드가 존재합니다.");
        this.cardId = cardId;
    }

    public void returnCard() {
        this.verifyIsRegisteredCard();
        this.cardId = null;
    }

    public void recordCardUsage(UsageHistory usageHistory) {
        this.verifyIsRegisteredCard();
        this.usageHistoryList.add(usageHistory);
    }

    private void verifyIsRegisteredCard() {
        if(this.cardId == null)
            throw new IllegalStateException("카드가 등록된 상태가 아닙니다.");
    }

}
