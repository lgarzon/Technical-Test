package com.lgarzona.controller;

import com.lgarzona.service.CustomerService;
import com.lgarzona.service.dto.CustomerCreateRequestDto;
import com.lgarzona.service.dto.CustomerResponseDto;
import com.lgarzona.service.dto.CustomerUpdateRequestDto;
import com.lgarzona.service.dto.CustomerUpdateStatusRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<List<CustomerResponseDto>> findAll(){
        var result = service.findAll();
        if(result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<CustomerResponseDto> findById(@PathVariable Long id) throws InterruptedException {
        var result = service.findByCustomerId(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<CustomerResponseDto> create(@Valid @RequestBody CustomerCreateRequestDto customerRequest) {
        var result = service.create(customerRequest);
        return ResponseEntity.created(URI.create("/" + result.getCustomerId())).body(result);
    }

    @PutMapping(value="/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<CustomerResponseDto> update(@PathVariable Long id, @Valid @RequestBody CustomerUpdateRequestDto customerRequest) {
        var result = service.update(id, customerRequest);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/{id}/status", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<CustomerResponseDto> updateStatus(@PathVariable Long id, @Valid @RequestBody CustomerUpdateStatusRequestDto customerRequest) {
        var result = service.updateStatus(id, customerRequest);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
