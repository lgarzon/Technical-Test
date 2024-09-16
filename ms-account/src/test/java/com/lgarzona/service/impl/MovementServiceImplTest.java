package com.lgarzona.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lgarzona.config.exception.ResourceNotFoundException;
import com.lgarzona.domain.MovementEntity;
import com.lgarzona.repository.MovementRepository;
import com.lgarzona.service.AccountService;
import com.lgarzona.service.dto.AccountResponseDto;
import com.lgarzona.service.dto.MovementCreateRequestDto;
import com.lgarzona.service.dto.MovementResponseDto;
import com.lgarzona.service.mapper.MovementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private MovementMapper mapper;

    @InjectMocks
    private MovementServiceImpl movementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    void testCreateMovementSuccess() {
        // Arrange
        Long accountId = 1L;
        Double amount = 500.0;
        MovementCreateRequestDto movementRequest = new MovementCreateRequestDto();
        movementRequest.setAccountId(accountId);
        movementRequest.setAmount(amount);

        AccountResponseDto account = new AccountResponseDto();
        account.setBalance(200.0);

        AccountResponseDto updatedAccount = new AccountResponseDto();
        updatedAccount.setBalance(100.0);

        MovementEntity movementEntity = new MovementEntity();
        movementEntity.setBalance(100.0);

        MovementResponseDto responseDto = new MovementResponseDto();

        when(accountService.findByAccountId(accountId)).thenReturn(account);
        when(accountService.updateBalance(accountId, amount)).thenReturn(updatedAccount);
        when(mapper.requestToEntity(movementRequest)).thenReturn(movementEntity);
        when(movementRepository.save(movementEntity)).thenReturn(movementEntity);
        when(mapper.entityToResponse(movementEntity)).thenReturn(responseDto);

        // Act
        MovementResponseDto result = movementService.create(movementRequest);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(accountService).findByAccountId(accountId);
        verify(accountService).updateBalance(accountId, amount);
        verify(movementRepository).save(movementEntity);
    }
}
