package com.galid.card_refund.domains.user.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "push_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PushTokenEntity extends BaseEntity {
    @Id @GeneratedValue
    private Long pushTokenId;

    private String pushToken;

    private Long userId;

    @Builder
    public PushTokenEntity(String pushToken, Long userId) {
        this.pushToken = pushToken;
        this.userId = userId;
    }

    public void updatePushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
