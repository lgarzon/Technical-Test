package com.lgarzona.controller;

import com.lgarzona.domain.AccountEntity;
import com.lgarzona.service.MovementService;
import com.lgarzona.service.dto.*;
import com.lgarzona.service.impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "v1/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementService service;

    private final CustomerServiceImpl customerService;

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<MovementResponseDto> create(@Valid @RequestBody MovementCreateRequestDto movementRequest) {
        var result = service.create(movementRequest);
        return ResponseEntity.created(URI.create("/" + result.getAccountId())).body(result);
    }

    @GetMapping("/{customerId}/report")
    public ResponseEntity<CustomerReportResponseDto> getCustomerAccountsWithMovements(
            @PathVariable Long customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        CustomerReportResponseDto accountsWithMovements = customerService.getCustomerAccountsWithMovements(customerId, startDate, endDate);
        return ResponseEntity.ok(accountsWithMovements);
    }
}
