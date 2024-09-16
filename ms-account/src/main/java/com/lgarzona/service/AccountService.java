package com.lgarzona.service;

import com.lgarzona.domain.AccountEntity;
import com.lgarzona.service.dto.AccountCreateRequestDto;
import com.lgarzona.service.dto.AccountResponseDto;
import com.lgarzona.service.dto.AccountUpdateRequestDto;
import com.lgarzona.service.dto.AccountUpdateStatusRequestDto;

import java.util.List;

public interface AccountService {
    List<AccountResponseDto> findAll();
    AccountResponseDto findByAccountId(Long accountId);
    AccountResponseDto create(AccountCreateRequestDto accountRequest);
    AccountResponseDto update(Long id, AccountUpdateRequestDto accountRequest);
    AccountResponseDto updateStatus(Long id, AccountUpdateStatusRequestDto accountRequest);
    AccountResponseDto updateBalance(Long id, Double amount);
    void delete(Long id);
    Double getAccountBalance(String accountNumber);
}
