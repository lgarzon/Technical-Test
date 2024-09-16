package com.lgarzona.service.impl;

import com.lgarzona.config.exception.ResourceNotFoundException;
import com.lgarzona.domain.AccountEntity;
import com.lgarzona.domain.MovementEntity;
import com.lgarzona.repository.MovementRepository;
import com.lgarzona.service.AccountService;
import com.lgarzona.service.MovementService;
import com.lgarzona.service.dto.AccountResponseDto;
import com.lgarzona.service.dto.MovementCreateRequestDto;
import com.lgarzona.service.dto.MovementResponseDto;
import com.lgarzona.service.mapper.MovementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final AccountService accountService;
    private final MovementMapper mapper;

    @Override
    @Transactional
    public MovementResponseDto create(MovementCreateRequestDto movementRequest) {

        AccountResponseDto account = accountService.findByAccountId(movementRequest.getAccountId());

        if (account.getBalance() + movementRequest.getAmount() < 0) {
            throw new ResourceNotFoundException("Insufficient balance");
        }

        AccountResponseDto accountBalance = accountService.updateBalance(movementRequest.getAccountId(), movementRequest.getAmount());

        MovementEntity entity = mapper.requestToEntity(movementRequest);
        entity.setBalance(accountBalance.getBalance());
        movementRepository.save(entity);
        return mapper.entityToResponse(entity);
    }
}
