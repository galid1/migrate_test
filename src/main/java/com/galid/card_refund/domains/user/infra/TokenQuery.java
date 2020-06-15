package com.galid.card_refund.domains.user.infra;

import com.galid.card_refund.domains.user.domain.ApiTokenEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.galid.card_refund.domains.user.domain.QApiTokenEntity.*;


@Component
public class TokenQuery {
    private JPAQueryFactory query;

    @Autowired
    public TokenQuery(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public Optional<ApiTokenEntity> getTokenBy(Long userId) {
        return Optional.ofNullable(query
                .selectFrom(apiTokenEntity)
                .where(apiTokenEntity.user.userId.eq(userId))
                .fetchOne());
    }
}
