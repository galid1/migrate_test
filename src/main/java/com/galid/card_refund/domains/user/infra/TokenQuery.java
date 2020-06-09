package com.galid.card_refund.domains.user.infra;

import com.galid.card_refund.domains.user.domain.QTokenEntity;
import com.galid.card_refund.domains.user.domain.TokenEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.galid.card_refund.domains.user.domain.QTokenEntity.*;

@Component
public class TokenQuery {
    private JPAQueryFactory query;

    @Autowired
    public TokenQuery(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public Optional<TokenEntity> getTokenBy(Long userId) {
        return Optional.ofNullable(query
                .selectFrom(tokenEntity)
                .where(tokenEntity.user.userId.eq(userId))
                .fetchOne());
    }
}
