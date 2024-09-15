package com.lgarzona.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CustomerResponseDto {

    private String name;

    private String gender;

    private int age;

    private String identification;

    private String adress;

    private String phone;

    private Long customerId;

    private String password;

    private Boolean status;
}
