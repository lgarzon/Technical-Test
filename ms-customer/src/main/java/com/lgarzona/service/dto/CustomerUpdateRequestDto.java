package com.lgarzona.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerUpdateRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String gender;

    @NotNull
    private int age;

    @NotBlank
    private String identification;

    @NotBlank
    private String adress;

    @NotBlank
    private String phone;

    @NotBlank
    private String password;
}
