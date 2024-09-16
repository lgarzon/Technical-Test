package com.lgarzona.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lgarzona.config.exception.ResourceNotFoundException;
import com.lgarzona.domain.CustomerEntity;
import com.lgarzona.repository.CustomerRepository;
import com.lgarzona.service.dto.*;
import com.lgarzona.service.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerMapper mapper;

    @InjectMocks
    private CustomerServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllReturnsListOfCustomerResponseDto() {
        CustomerEntity entity1 = new CustomerEntity();
        CustomerEntity entity2 = new CustomerEntity();
        CustomerResponseDto dto1 = new CustomerResponseDto();
        CustomerResponseDto dto2 = new CustomerResponseDto();

        when(repository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(mapper.entityToResponse(entity1)).thenReturn(dto1);
        when(mapper.entityToResponse(entity2)).thenReturn(dto2);

        List<CustomerResponseDto> result = service.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void findByCustomerIdReturnsCustomerResponseDto() {
        Long customerId = 1L;
        CustomerEntity entity = new CustomerEntity();
        CustomerResponseDto dto = new CustomerResponseDto();

        when(repository.findById(customerId)).thenReturn(Optional.of(entity));
        when(mapper.entityToResponse(entity)).thenReturn(dto);

        CustomerResponseDto result = service.findByCustomerId(customerId);

        assertEquals(dto, result);
    }

    @Test
    void findByCustomerIdThrowsResourceNotFoundException() {
        Long customerId = 1L;

        when(repository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findByCustomerId(customerId));
    }

    @Test
    void createSavesAndReturnsCustomerResponseDto() {
        CustomerCreateRequestDto request = new CustomerCreateRequestDto();
        CustomerEntity entity = new CustomerEntity();
        CustomerResponseDto dto = new CustomerResponseDto();

        when(mapper.requestToEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToResponse(entity)).thenReturn(dto);

        CustomerResponseDto result = service.create(request);

        assertEquals(dto, result);
    }

    @Test
    void updateSavesAndReturnsCustomerResponseDto() {
        Long id = 1L;
        CustomerUpdateRequestDto request = new CustomerUpdateRequestDto();
        CustomerEntity entity = new CustomerEntity();
        CustomerResponseDto dto = new CustomerResponseDto();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToResponse(entity)).thenReturn(dto);

        CustomerResponseDto result = service.update(id, request);

        assertEquals(dto, result);
    }

    @Test
    void updateStatusSavesAndReturnsCustomerResponseDto() {
        Long id = 1L;
        CustomerUpdateStatusRequestDto request = new CustomerUpdateStatusRequestDto();
        CustomerEntity entity = new CustomerEntity();
        CustomerResponseDto dto = new CustomerResponseDto();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.entityToResponse(entity)).thenReturn(dto);

        CustomerResponseDto result = service.updateStatus(id, request);

        assertEquals(dto, result);
    }

    @Test
    void deleteRemovesCustomerEntity() {
        Long id = 1L;
        CustomerEntity entity = new CustomerEntity();

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.delete(id);

        verify(repository, times(1)).delete(entity);
    }

    @Test
    void deleteThrowsResourceNotFoundException() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
    }
}
