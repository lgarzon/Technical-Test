package com.lgarzona.service;

import com.lgarzona.service.dto.CustomerReportResponseDto;
import com.lgarzona.service.dto.CustomerResponseDto;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public interface CustomerService {

    CustomerReportResponseDto getCustomerAccountsWithMovements(Long customerId, LocalDateTime startDate, LocalDateTime endDate);
    CustomerResponseDto findById(Long id);
    public CompletableFuture<CustomerResponseDto> findByIdResilience(Long id);
    public CompletableFuture<CustomerResponseDto> findByIdResilienceFallBackMethod(Long id, Throwable t);
}
