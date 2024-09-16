package com.lgarzona.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccountUpdateRequestDto {

    @NotBlank
    private String accountNumber;

    @NotBlank
    private String accountType;

    @NotNull
    private Double balance;

    @NotNull
    private Long customerId;
}
