package com.galid.card_refund.domains.user.domain;

import com.galid.card_refund.common.config.logging.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenEntity extends BaseEntity {
    @Id @GeneratedValue
    private Long tokenId;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public TokenEntity(String token, UserEntity user) {
        this.token = token;
        this.user = user;
    }
}
