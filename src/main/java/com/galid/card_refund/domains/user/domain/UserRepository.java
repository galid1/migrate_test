package com.galid.card_refund.domains.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByDeviceId(String deviceId);

    interface UserIdModel {
        Long getUserId();
    }

    @Query(
            value="select user_id as userId " +
                    "from user as u " +
                    "inner join card as c " +
                    "on u.card_id = c.card_id " +
                    "where RIGHT(c.card_num, 4) = ?1",
            nativeQuery = true
    )
    Optional<UserIdModel> findByLastFourDigitOfCardNum(String lastFourDigitOfCardNum);
}
