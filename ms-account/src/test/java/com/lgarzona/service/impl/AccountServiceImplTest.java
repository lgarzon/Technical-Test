package com.lgarzona.service.impl;

import com.lgarzona.config.exception.ResourceNotFoundException;
import com.lgarzona.domain.AccountEntity;
import com.lgarzona.repository.AccountRepository;
import com.lgarzona.service.dto.AccountCreateRequestDto;
import com.lgarzona.service.dto.AccountResponseDto;
import com.lgarzona.service.dto.AccountUpdateRequestDto;
import com.lgarzona.service.dto.AccountUpdateStatusRequestDto;
import com.lgarzona.service.mapper.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapper mapper;

    @InjectMocks
    private AccountServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllReturnsListOfAccountResponseDto() {
        AccountEntity entity = new AccountEntity();
        AccountResponseDto responseDto = new AccountResponseDto();
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));
        when(mapper.entityToResponse(entity)).thenReturn(responseDto);

        List<AccountResponseDto> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(responseDto, result.getFirst());
    }

    @Test
    void findByAccountIdReturnsAccountResponseDto() {
        Long accountId = 1L;
        AccountEntity entity = new AccountEntity();
        AccountResponseDto responseDto = new AccountResponseDto();
        when(repository.findById(accountId)).thenReturn(Optional.of(entity));
        when(mapper.entityToResponse(entity)).thenReturn(responseDto);

        AccountResponseDto result = service.findByAccountId(accountId);

        assertEquals(responseDto, result);
    }

    @Test
    void findByAccountIdThrowsResourceNotFoundException() {
        Long accountId = 1L;
        when(repository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findByAccountId(accountId));
    }

    @Test
    void createReturnsAccountResponseDto() {
        AccountCreateRequestDto requestDto = new AccountCreateRequestDto();
        AccountEntity entity = new AccountEntity();
        AccountResponseDto responseDto = new AccountResponseDto();
        when(mapper.requestToEntity(requestDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToResponse(entity)).thenReturn(responseDto);

        AccountResponseDto result = service.create(requestDto);

        assertEquals(responseDto, result);
    }

    @Test
    void updateReturnsAccountResponseDto() {
        Long id = 1L;
        AccountUpdateRequestDto requestDto = new AccountUpdateRequestDto();
        AccountEntity entity = new AccountEntity();
        AccountResponseDto responseDto = new AccountResponseDto();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToResponse(entity)).thenReturn(responseDto);

        AccountResponseDto result = service.update(id, requestDto);

        assertEquals(responseDto, result);
    }

    @Test
    void updateStatusReturnsAccountResponseDto() {
        Long id = 1L;
        AccountUpdateStatusRequestDto requestDto = new AccountUpdateStatusRequestDto();
        AccountEntity entity = new AccountEntity();
        AccountResponseDto responseDto = new AccountResponseDto();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToResponse(entity)).thenReturn(responseDto);

        AccountResponseDto result = service.updateStatus(id, requestDto);

        assertEquals(responseDto, result);
    }

    @Test
    void deleteRemovesAccountEntity() {
        Long id = 1L;
        AccountEntity entity = new AccountEntity();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.delete(id);

        verify(repository, times(1)).delete(entity);
    }
}
