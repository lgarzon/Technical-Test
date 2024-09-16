package com.lgarzona.service.impl;

import com.lgarzona.config.exception.ResourceNotFoundException;
import com.lgarzona.domain.AccountEntity;
import com.lgarzona.repository.AccountRepository;
import com.lgarzona.service.AccountService;
import com.lgarzona.service.dto.AccountCreateRequestDto;
import com.lgarzona.service.dto.AccountResponseDto;
import com.lgarzona.service.dto.AccountUpdateRequestDto;
import com.lgarzona.service.dto.AccountUpdateStatusRequestDto;
import com.lgarzona.service.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    @Override
    public List<AccountResponseDto> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponseDto findByAccountId(Long accountId) {
        return repository.findById(accountId)
                .map(mapper::entityToResponse).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    @Override
    public AccountResponseDto create(AccountCreateRequestDto accountRequest) {
        AccountEntity entity = mapper.requestToEntity(accountRequest);
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    public AccountResponseDto update(Long id, AccountUpdateRequestDto accountRequest) {
        AccountEntity entity = getAccountById(id);
        BeanUtils.copyProperties(accountRequest, entity);
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    public AccountResponseDto updateStatus(Long id, AccountUpdateStatusRequestDto accountRequest) {
        AccountEntity entity = getAccountById(id);
        entity.setStatus(accountRequest.getStatus());
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    public void delete(Long id) {
        AccountEntity entity = getAccountById(id);
        repository.delete(entity);
    }

    private AccountEntity getAccountById(Long id) {
        Optional<AccountEntity> accountEntityOp = repository.findById(id);
        if(accountEntityOp.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return accountEntityOp.get();
    }
}
