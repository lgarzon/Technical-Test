package com.lgarzona.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.lgarzona.service.CustomerService;
import com.lgarzona.service.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class CustomerControllerTest {

    @Mock
    private CustomerService service;

    @InjectMocks
    private CustomerController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllReturnsOkWhenCustomersExist() {
        CustomerResponseDto dto1 = new CustomerResponseDto();
        CustomerResponseDto dto2 = new CustomerResponseDto();
        when(service.findAll()).thenReturn(List.of(dto1, dto2));

        ResponseEntity<List<CustomerResponseDto>> response = controller.findAll();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void findAllReturnsNoContentWhenNoCustomersExist() {
        when(service.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CustomerResponseDto>> response = controller.findAll();

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void findByIdReturnsOkWhenCustomerExists() throws InterruptedException {
        Long id = 1L;
        CustomerResponseDto dto = new CustomerResponseDto();
        when(service.findByCustomerId(id)).thenReturn(dto);

        ResponseEntity<CustomerResponseDto> response = controller.findById(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
    }

    @Test
    void createReturnsCreatedWithLocationHeader() {
        CustomerCreateRequestDto request = new CustomerCreateRequestDto();
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setCustomerId(1L);
        when(service.create(request)).thenReturn(dto);

        ResponseEntity<CustomerResponseDto> response = controller.create(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(URI.create("/1"), response.getHeaders().getLocation());
        assertEquals(dto, response.getBody());
    }

    @Test
    void updateReturnsOkWhenCustomerUpdated() {
        Long id = 1L;
        CustomerUpdateRequestDto request = new CustomerUpdateRequestDto();
        CustomerResponseDto dto = new CustomerResponseDto();
        when(service.update(id, request)).thenReturn(dto);

        ResponseEntity<CustomerResponseDto> response = controller.update(id, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
    }

    @Test
    void updateStatusReturnsOkWhenStatusUpdated() {
        Long id = 1L;
        CustomerUpdateStatusRequestDto request = new CustomerUpdateStatusRequestDto();
        CustomerResponseDto dto = new CustomerResponseDto();
        when(service.updateStatus(id, request)).thenReturn(dto);

        ResponseEntity<CustomerResponseDto> response = controller.updateStatus(id, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deleteReturnsNoContentWhenCustomerDeleted() {
        Long id = 1L;

        ResponseEntity<?> response = controller.delete(id);

        verify(service, times(1)).delete(id);
        assertEquals(204, response.getStatusCode().value());
    }
}
