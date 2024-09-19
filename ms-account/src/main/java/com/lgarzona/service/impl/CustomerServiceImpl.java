package com.lgarzona.service.impl;

import com.lgarzona.client.CustomerClientRest;
import com.lgarzona.config.exception.ResourceNotFoundException;
import com.lgarzona.domain.AccountEntity;
import com.lgarzona.domain.MovementEntity;
import com.lgarzona.repository.AccountRepository;
import com.lgarzona.repository.MovementRepository;
import com.lgarzona.service.CustomerService;
import com.lgarzona.service.dto.AccountReportResponseDto;
import com.lgarzona.service.dto.CustomerReportResponseDto;
import com.lgarzona.service.dto.CustomerResponseDto;
import com.lgarzona.service.dto.MovementReportResponseDto;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;
    private final CustomerClientRest client;

    @Override
    public CustomerReportResponseDto getCustomerAccountsWithMovements(Long customerId, LocalDateTime startDate, LocalDateTime endDate) {

        List<AccountEntity> accounts = accountRepository.findByCustomerId(customerId);
        List<Long> accountIds = accounts.stream()
                .map(AccountEntity::getAccountId)
                .collect(Collectors.toList());

        // Obtener movimientos como entidades
        List<MovementEntity> movements = movementRepository.findMovementsByAccountsAndDateRange(accountIds, startDate, endDate);

        // Agrupar movimientos por ID de cuenta
        Map<Long, List<MovementEntity>> movementsByAccountId = movements.stream()
                .collect(Collectors.groupingBy(m -> m.getAccount().getAccountId()));

        // Transformar movimientos a DTOs
        Map<Long, List<MovementReportResponseDto>> movementsByAccountIdDto = movementsByAccountId.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(m -> new MovementReportResponseDto(m.getType(), m.getAmount(), m.getBalance(), m.getCreationDate()))
                                .collect(Collectors.toList())
                ));

        // Mapear cuentas a DTOs
        List<AccountReportResponseDto> accountDtos = accounts.stream().map(account -> {
            AccountReportResponseDto accountDto = new AccountReportResponseDto();
            accountDto.setAccountNumber(account.getAccountNumber());
            accountDto.setAccountType(account.getAccountType());
            accountDto.setBalance(account.getBalance());
            accountDto.setMovements(movementsByAccountIdDto.getOrDefault(account.getAccountId(), Collections.emptyList()));
            return accountDto;
        }).collect(Collectors.toList());

        CompletableFuture<CustomerResponseDto> customerCF = findByIdResilience(customerId);
        CustomerResponseDto customer = null;

        try {
            customer = customerCF.get();
        } catch (InterruptedException | ExecutionException e) {
            log.warn("customerCF.get(): {}", e.getMessage(), e);
        }

        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found");
        }

        CustomerReportResponseDto responseDto = new CustomerReportResponseDto();
        responseDto.setIdentification(customer.getIdentification());
        responseDto.setName(customer.getName());
        responseDto.setAccounts(accountDtos);

        return responseDto;

    }

    @Override
    public CustomerResponseDto findById(Long id) {
        try {
            return client.findById(id);
        }catch (FeignException e) {
            if(e.status() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }else {
                throw e;
            }
        }
    }

    @CircuitBreaker(name="customer", fallbackMethod = "findByIdResilienceFallBackMethod")
    @Override
    public CompletableFuture<CustomerResponseDto> findByIdResilience(Long id) {
        log.info("findByIdResilience");
        return CompletableFuture.supplyAsync(()-> {
                    try {
                        return client.findById(id);
                    } catch (FeignException e) {
                        if(e.status() == HttpStatus.NOT_FOUND.value()) {
                            return null;
                        }else{
                            throw e;
                        }
                    }
                }
        );

    }

    @Override
    public CompletableFuture<CustomerResponseDto> findByIdResilienceFallBackMethod(Long id, Throwable t) {
        log.info("findByIdResilienceFallBackMethod: {}", t.getMessage());
        return CompletableFuture.supplyAsync(()-> {
            throw new ResourceNotFoundException("Customer service is not available");
        });
    }
}
