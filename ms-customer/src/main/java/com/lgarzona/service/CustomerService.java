package com.lgarzona.service;

import com.lgarzona.service.dto.CustomerCreateRequestDto;
import com.lgarzona.service.dto.CustomerResponseDto;
import com.lgarzona.service.dto.CustomerUpdateRequestDto;
import com.lgarzona.service.dto.CustomerUpdateStatusRequestDto;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDto> findAll();
    List<CustomerResponseDto> findByCustomerId(Long customerId);
    CustomerResponseDto create(CustomerCreateRequestDto customerRequest);
    CustomerResponseDto update(Long id, CustomerUpdateRequestDto customerRequest);
    CustomerResponseDto updateStatus(Long id, CustomerUpdateStatusRequestDto customerRequest);
    void delete(Long id);
}
