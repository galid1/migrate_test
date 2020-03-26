package com.galid.card_refund.domains.user.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {
    @Id @GeneratedValue
    private Long userId;

    private String deviceId;
    private String nickname;

    private Long userCardId;

    @Builder
    public UserEntity(String deviceId, String nickname) {
        this.setDeviceId(deviceId);
        this.setNickname(nickname);
    }

    private void setDeviceId(String deviceId) {
        if (deviceId == null)
            throw new IllegalArgumentException("Device ID를 입력하세요.");

        this.deviceId = deviceId;
    }

    private void setNickname(String nickname) {
        if(nickname == null || nickname.length() < 3)
            throw new IllegalArgumentException("Nickname은 3글자 이상이어야합니다.");
        this.nickname = nickname;
    }

    public void registerCard(long userCardId) {
        if(this.userCardId != null)
            throw new IllegalStateException("이미 등록된 카드가 존재합니다.");
        this.userCardId = userCardId;
    }

    public void returnCard() {
        if(this.userCardId == null)
            throw new IllegalStateException("등록된 카드가 존재하지 않습니다.");
        this.userCardId = null;
    }
}
