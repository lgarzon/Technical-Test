package com.lgarzona.service.impl;

import com.lgarzona.config.exception.ResourceNotFoundException;
import com.lgarzona.domain.AccountEntity;
import com.lgarzona.repository.AccountRepository;
import com.lgarzona.service.AccountService;
import com.lgarzona.service.CustomerService;
import com.lgarzona.service.dto.*;
import com.lgarzona.service.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final CustomerService customerService;

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto findByAccountId(Long accountId) {
        return repository.findById(accountId)
                .map(mapper::entityToResponse).orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }

    @Override
    @Transactional
    public AccountResponseDto create(AccountCreateRequestDto accountRequest) {
        getCustomerById(accountRequest.getCustomerId());
        AccountEntity entity = mapper.requestToEntity(accountRequest);
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    @Transactional
    public AccountResponseDto update(Long id, AccountUpdateRequestDto accountRequest) {
        getCustomerById(accountRequest.getCustomerId());
        AccountEntity entity = getAccountById(id);
        BeanUtils.copyProperties(accountRequest, entity);
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    @Transactional
    public AccountResponseDto updateStatus(Long id, AccountUpdateStatusRequestDto accountRequest) {
        AccountEntity entity = getAccountById(id);
        entity.setStatus(accountRequest.getStatus());
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    @Transactional
    public AccountResponseDto updateBalance(Long id, Double amount) {
        AccountEntity entity = getAccountById(id);
        entity.setBalance(entity.getBalance() + amount);
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        AccountEntity entity = getAccountById(id);
        repository.delete(entity);
    }

    @Override
    public Double getAccountBalance(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).getBalance();
    }

    private AccountEntity getAccountById(Long id) {
        Optional<AccountEntity> accountEntityOp = repository.findById(id);
        if(accountEntityOp.isEmpty()) {
            throw new ResourceNotFoundException("Account not found");
        }
        return accountEntityOp.get();
    }

    private CustomerResponseDto getCustomerById(Long id) {
        CompletableFuture<CustomerResponseDto> customerCF = customerService.findByIdResilience(id);
        CustomerResponseDto customer = null;

        try {
            customer = customerCF.get();
        } catch (InterruptedException | ExecutionException e) {
            log.warn("customerCF.get(): {}", e.getMessage(), e);
        }

        if(customer == null) {
            throw new ResourceNotFoundException("Customer not found");
        }

        return customer;
    }
}
