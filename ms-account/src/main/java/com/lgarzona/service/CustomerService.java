package com.lgarzona.service;

import com.lgarzona.service.dto.CustomerReportResponseDto;
import com.lgarzona.service.dto.CustomerResponseDto;

import java.time.LocalDateTime;

public interface CustomerService {

    CustomerReportResponseDto getCustomerAccountsWithMovements(Long customerId, LocalDateTime startDate, LocalDateTime endDate);
    CustomerResponseDto findById(Long id);
}
