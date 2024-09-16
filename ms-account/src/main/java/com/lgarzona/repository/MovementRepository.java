package com.lgarzona.repository;

import com.lgarzona.domain.MovementEntity;
import org.springframework.data.repository.CrudRepository;

public interface MovementRepository extends CrudRepository<MovementEntity, Long> {
}
