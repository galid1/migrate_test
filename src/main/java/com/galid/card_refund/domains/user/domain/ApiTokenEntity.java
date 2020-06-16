package com.galid.card_refund.domains.user.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiTokenEntity extends BaseEntity {
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
