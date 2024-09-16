package com.lgarzona.service.impl;

import com.lgarzona.config.exception.ResourceNotFoundException;
import com.lgarzona.domain.CustomerEntity;
import com.lgarzona.repository.CustomerRepository;
import com.lgarzona.service.CustomerService;
import com.lgarzona.service.dto.CustomerCreateRequestDto;
import com.lgarzona.service.dto.CustomerResponseDto;
import com.lgarzona.service.dto.CustomerUpdateRequestDto;
import com.lgarzona.service.dto.CustomerUpdateStatusRequestDto;
import com.lgarzona.service.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .map(mapper::entityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto findByCustomerId(Long customerId) {
        return repository.findById(customerId)
                .map(mapper::entityToResponse).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }

    @Override
    @Transactional
    public CustomerResponseDto create(CustomerCreateRequestDto customerRequest) {
        CustomerEntity entity = mapper.requestToEntity(customerRequest);
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    @Transactional
    public CustomerResponseDto update(Long id, CustomerUpdateRequestDto customerRequest) {
        CustomerEntity entity = getCustomerById(id);
        BeanUtils.copyProperties(customerRequest, entity);
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    @Transactional
    public CustomerResponseDto updateStatus(Long id, CustomerUpdateStatusRequestDto customerRequest) {
        CustomerEntity entity = getCustomerById(id);
        entity.setStatus(customerRequest.getStatus());
        repository.save(entity);
        return mapper.entityToResponse(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CustomerEntity entity = getCustomerById(id);
        repository.delete(entity);
    }

    private CustomerEntity getCustomerById(Long id) {
        Optional<CustomerEntity> customerEntityOp = repository.findById(id);
        if(customerEntityOp.isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        return customerEntityOp.get();
    }
}
