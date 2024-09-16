package com.lgarzona.repository;

import com.lgarzona.domain.AccountEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    AccountEntity findByAccountNumber(String accountNumber);

    List<AccountEntity> findByCustomerId(Long customerId);
}
