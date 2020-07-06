package com.galid.card_refund.domains.user.domain;

import com.galid.card_refund.common.logging.BaseAuditEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "api_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiTokenEntity extends BaseAuditEntity {
    @Id @GeneratedValue
    private Long tokenId;

    private String apiToken;

    private Long userId;

    @Builder
    public ApiTokenEntity(String apiToken, Long userId) {
        this.apiToken = apiToken;
        this.userId = userId;
    }
}
