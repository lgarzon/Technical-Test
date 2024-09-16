package com.lgarzona.controller;

import com.lgarzona.service.MovementService;
import com.lgarzona.service.dto.AccountCreateRequestDto;
import com.lgarzona.service.dto.AccountResponseDto;
import com.lgarzona.service.dto.MovementCreateRequestDto;
import com.lgarzona.service.dto.MovementResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(path = "v1/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementService service;

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<MovementResponseDto> create(@Valid @RequestBody MovementCreateRequestDto movementRequest) {
        var result = service.create(movementRequest);
        return ResponseEntity.created(URI.create("/" + result.getAccountId())).body(result);
    }
}
