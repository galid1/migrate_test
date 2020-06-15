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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public ApiTokenEntity(String apiToken, UserEntity user) {
        this.apiToken = apiToken;
        this.user = user;
    }
}
