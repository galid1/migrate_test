package com.galid.card_refund.domains.user.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import com.galid.card_refund.domains.card.domain.CardEntity;
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

    @Embedded
    private UserPassportInformation userPassportInformation;
    @Enumerated(value = EnumType.STRING)
    private UserPassportStatus passportStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private CardEntity card;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_card_usage_history",
            joinColumns = @JoinColumn(name = "user_id"))
    private List<UsageHistory> usageHistoryList = new ArrayList<>();

    @Builder
    public UserEntity(String deviceId, String nickname) {
        this.setDeviceId(deviceId);
        this.setNickname(nickname);
        this.passportStatus = UserPassportStatus.SUCCESS_STATUS;
    }

    private void setDeviceId(String deviceId) {
        if (deviceId == null || deviceId.length() < 1)
            throw new IllegalArgumentException("Device ID를 입력하세요.");

        this.deviceId = deviceId;
    }

    private void setNickname(String nickname) {
        if(nickname == null || nickname.length() < 0)
            throw new IllegalArgumentException("Nickname은 1글자 이상이어야합니다.");
        this.nickname = nickname;
    }

    public void registerCard(CardEntity card) {
        if(this.card != null)
            throw new IllegalStateException("이미 등록된 카드가 존재합니다.");
        this.card = card;
    }

    public void returnCard() {
        this.verifyCardIsRegistered();
        this.card = null;
    }

    public void recordCardUsage(UsageHistory usageHistory) {
        this.verifyCardIsRegistered();
        this.usageHistoryList.add(usageHistory);
        this.card.recordRemainAmount(usageHistory.getRemainAmount());
    }

    private void verifyCardIsRegistered() {
        if(this.card == null)
            throw new IllegalStateException("카드가 등록된 상태가 아닙니다.");
    }

    public CardEntity getCard() {
        verifyCardIsRegistered();
        return this.card;
    }

    public void estimatePassport(UserPassportStatus passportStatus, UserPassportInformation userPassportInformation) {
        this.passportStatus = passportStatus;
        this.userPassportInformation = userPassportInformation;
    }

    public void uploadPassportImagePath(String passportImagePath) {
        this.passPortImagePath = passportImagePath;
    }

    public void updateUserInformation(String nickname, String passportImagePath) {
        if(nickname != null && nickname.length() > 1)
            this.nickname = nickname;

        if(passportImagePath != null && passportImagePath.length() > 1)
            this.passPortImagePath = passportImagePath;
    }
}
