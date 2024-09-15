package com.lgarzona.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonEntity implements Serializable {

    private String name;

    private String gender;

    private int age;

    private String identification;

    private String adress;

    private String phone;
}
