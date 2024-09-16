package com.lgarzona.client;

import com.lgarzona.service.dto.CustomerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-customer", url = "http://localhost:8080", path = "/v1/customers")
public interface CustomerClientRest {

    @GetMapping("/{id}")
    public CustomerResponseDto findById(@PathVariable Long id);
}
