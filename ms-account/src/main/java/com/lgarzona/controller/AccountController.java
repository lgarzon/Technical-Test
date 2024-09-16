package com.lgarzona.controller;

import com.lgarzona.service.AccountService;
import com.lgarzona.service.dto.AccountCreateRequestDto;
import com.lgarzona.service.dto.AccountResponseDto;
import com.lgarzona.service.dto.AccountUpdateRequestDto;
import com.lgarzona.service.dto.AccountUpdateStatusRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<List<AccountResponseDto>> findAll(){
        var result = service.findAll();
        if(result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<AccountResponseDto> findById(@PathVariable Long id) throws InterruptedException {
        var result = service.findByAccountId(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<AccountResponseDto> create(@Valid @RequestBody AccountCreateRequestDto accountRequest) {
        var result = service.create(accountRequest);
        return ResponseEntity.created(URI.create("/" + result.getAccountId())).body(result);
    }

    @PutMapping(value="/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<AccountResponseDto> update(@PathVariable Long id, @Valid @RequestBody AccountUpdateRequestDto accountRequest) {
        var result = service.update(id, accountRequest);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/{id}/status", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
    public ResponseEntity<AccountResponseDto> updateStatus(@PathVariable Long id, @Valid @RequestBody AccountUpdateStatusRequestDto accountRequest) {
        var result = service.updateStatus(id, accountRequest);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
