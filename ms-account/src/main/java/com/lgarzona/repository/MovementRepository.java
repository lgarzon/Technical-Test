package com.lgarzona.repository;

import com.lgarzona.domain.MovementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementRepository extends CrudRepository<MovementEntity, Long> {

    @Query("SELECT m FROM MovementEntity m WHERE m.account.accountId IN :accountIds AND m.creationDate BETWEEN :startDate AND :endDate")
    List<MovementEntity> findMovementsByAccountsAndDateRange(
            @Param("accountIds") List<Long> accountIds,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
