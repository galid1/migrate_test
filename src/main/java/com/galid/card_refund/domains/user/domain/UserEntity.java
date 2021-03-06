package com.galid.card_refund.domains.user.domain;

import com.galid.card_refund.common.logging.BaseAuditEntity;
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
public class UserEntity extends BaseAuditEntity {
    @Id @GeneratedValue
    private Long userId;

    private String deviceId;
    private String nickname;
    private String passPortImagePath;
    @Enumerated(value = EnumType.STRING)
    private UserPassportStatus passportStatus;

    @Embedded
    private UserPassportInformation userPassportInformation;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_card_usage_history",
            joinColumns = @JoinColumn(name = "user_id"))
    private List<UsageHistory> usageHistoryList = new ArrayList<>();

    @Builder
    public UserEntity(String deviceId, String nickname) {
        this.setDeviceId(deviceId);
        this.setNickname(nickname);
        this.passportStatus = UserPassportStatus.ESTIMATING_STATUS;
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

    public void recordCardUsage(UsageHistory usageHistory) {
        this.usageHistoryList.add(usageHistory);
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
