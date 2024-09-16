package com.lgarzona.service.impl;

import com.lgarzona.domain.AccountEntity;
import com.lgarzona.domain.MovementEntity;
import com.lgarzona.repository.AccountRepository;
import com.lgarzona.repository.MovementRepository;
import com.lgarzona.service.dto.AccountReportResponseDto;
import com.lgarzona.service.dto.CustomerReportResponseDto;
import com.lgarzona.service.dto.MovementReportResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

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

        CustomerReportResponseDto responseDto = new CustomerReportResponseDto();
        responseDto.setCustomerId(customerId);
        responseDto.setAccounts(accountDtos);

        return responseDto;

    }
}
