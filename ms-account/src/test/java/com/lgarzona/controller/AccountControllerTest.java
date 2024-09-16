package com.lgarzona.controller;

import com.lgarzona.service.AccountService;
import com.lgarzona.service.dto.AccountCreateRequestDto;
import com.lgarzona.service.dto.AccountResponseDto;
import com.lgarzona.service.dto.AccountUpdateRequestDto;
import com.lgarzona.service.dto.AccountUpdateStatusRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AccountControllerTest {

    @Mock
    private AccountService service;

    @InjectMocks
    private AccountController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllReturnsOkWithListOfAccounts() {
        AccountResponseDto responseDto = new AccountResponseDto();
        when(service.findAll()).thenReturn(Collections.singletonList(responseDto));

        ResponseEntity<List<AccountResponseDto>> result = controller.findAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, Objects.requireNonNull(result.getBody()).size());
        assertEquals(responseDto, result.getBody().getFirst());
    }

    @Test
    void findAllReturnsNoContentWhenEmpty() {
        when(service.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<AccountResponseDto>> result = controller.findAll();

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void findByIdReturnsOkWithAccount() throws InterruptedException {
        Long accountId = 1L;
        AccountResponseDto responseDto = new AccountResponseDto();
        when(service.findByAccountId(accountId)).thenReturn(responseDto);

        ResponseEntity<AccountResponseDto> result = controller.findById(accountId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void createReturnsCreatedWithAccount() {
        AccountCreateRequestDto requestDto = new AccountCreateRequestDto();
        AccountResponseDto responseDto = new AccountResponseDto();
        responseDto.setAccountId(1L);
        when(service.create(requestDto)).thenReturn(responseDto);

        ResponseEntity<AccountResponseDto> result = controller.create(requestDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(URI.create("/1"), result.getHeaders().getLocation());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void updateReturnsOkWithUpdatedAccount() {
        Long accountId = 1L;
        AccountUpdateRequestDto requestDto = new AccountUpdateRequestDto();
        AccountResponseDto responseDto = new AccountResponseDto();
        when(service.update(accountId, requestDto)).thenReturn(responseDto);

        ResponseEntity<AccountResponseDto> result = controller.update(accountId, requestDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void updateStatusReturnsOkWithUpdatedAccount() {
        Long accountId = 1L;
        AccountUpdateStatusRequestDto requestDto = new AccountUpdateStatusRequestDto();
        AccountResponseDto responseDto = new AccountResponseDto();
        when(service.updateStatus(accountId, requestDto)).thenReturn(responseDto);

        ResponseEntity<AccountResponseDto> result = controller.updateStatus(accountId, requestDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void deleteReturnsNoContent() {
        Long accountId = 1L;

        ResponseEntity<?> result = controller.delete(accountId);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }
}
