package com.lgarzona.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lgarzona.client.CustomerClientRest;
import com.lgarzona.config.exception.ResourceNotFoundException;
import com.lgarzona.domain.AccountEntity;
import com.lgarzona.domain.MovementEntity;
import com.lgarzona.repository.AccountRepository;
import com.lgarzona.repository.MovementRepository;
import com.lgarzona.service.dto.CustomerReportResponseDto;
import com.lgarzona.service.dto.CustomerResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CustomerServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerClientRest client;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCustomerAccountsWithMovementsSuccess() {
        // Arrange
        Long customerId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1L);
        accountEntity.setAccountNumber("123456");
        accountEntity.setAccountType("SAVINGS");
        accountEntity.setBalance(1000.0);

        MovementEntity movementEntity = new MovementEntity();
        movementEntity.setAmount(200.0);
        movementEntity.setBalance(1200.0);
        movementEntity.setType("DEPOSIT");
        movementEntity.setCreationDate(LocalDateTime.now());
        movementEntity.setAccount(accountEntity);

        CustomerResponseDto customerDto = new CustomerResponseDto();
        customerDto.setIdentification("ID123");
        customerDto.setName("John Doe");

        when(accountRepository.findByCustomerId(customerId)).thenReturn(List.of(accountEntity));
        when(movementRepository.findMovementsByAccountsAndDateRange(List.of(1L), startDate, endDate))
                .thenReturn(List.of(movementEntity));
        when(client.findById(customerId)).thenReturn(customerDto);

        // Act
        CustomerReportResponseDto result = customerService.getCustomerAccountsWithMovements(customerId, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals("ID123", result.getIdentification());
        assertEquals("John Doe", result.getName());
        assertEquals(1, result.getAccounts().size());
        assertEquals("123456", result.getAccounts().getFirst().getAccountNumber());
        assertEquals(1, result.getAccounts().getFirst().getMovements().size());
        assertEquals(200.0, result.getAccounts().getFirst().getMovements().getFirst().getAmount());
        verify(accountRepository).findByCustomerId(customerId);
        verify(movementRepository).findMovementsByAccountsAndDateRange(List.of(1L), startDate, endDate);
        verify(client).findById(customerId);
    }

    @Test
    void testGetCustomerAccountsWithMovementsCustomerNotFound() {
        // Arrange
        Long customerId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();

        when(accountRepository.findByCustomerId(customerId)).thenReturn(Collections.emptyList());
        when(client.findById(customerId)).thenReturn(null);

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> customerService.getCustomerAccountsWithMovements(customerId, startDate, endDate)
        );
        assertTrue(thrown.getMessage().contains("Customer not found"));
        verify(accountRepository).findByCustomerId(customerId);
        verify(client).findById(customerId);
    }

    @Test
    void testGetCustomerAccountsWithMovementsNoMovements() {
        // Arrange
        Long customerId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        LocalDateTime endDate = LocalDateTime.now();

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1L);
        accountEntity.setAccountNumber("123456");
        accountEntity.setAccountType("SAVINGS");
        accountEntity.setBalance(1000.0);

        CustomerResponseDto customerDto = new CustomerResponseDto();
        customerDto.setIdentification("ID123");
        customerDto.setName("John Doe");

        when(accountRepository.findByCustomerId(customerId)).thenReturn(List.of(accountEntity));
        when(movementRepository.findMovementsByAccountsAndDateRange(List.of(1L), startDate, endDate))
                .thenReturn(Collections.emptyList());
        when(client.findById(customerId)).thenReturn(customerDto);

        // Act
        CustomerReportResponseDto result = customerService.getCustomerAccountsWithMovements(customerId, startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals("ID123", result.getIdentification());
        assertEquals("John Doe", result.getName());
        assertEquals(1, result.getAccounts().size());
        assertEquals("123456", result.getAccounts().getFirst().getAccountNumber());
        assertTrue(result.getAccounts().getFirst().getMovements().isEmpty());
        verify(accountRepository).findByCustomerId(customerId);
        verify(movementRepository).findMovementsByAccountsAndDateRange(List.of(1L), startDate, endDate);
        verify(client).findById(customerId);
    }
}
