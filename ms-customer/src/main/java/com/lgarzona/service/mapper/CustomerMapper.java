package com.lgarzona.service.mapper;

import com.lgarzona.domain.CustomerEntity;
import com.lgarzona.service.dto.CustomerCreateRequestDto;
import com.lgarzona.service.dto.CustomerResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.gender", target = "gender")
    @Mapping(source = "entity.age", target = "age")
    @Mapping(source = "entity.identification", target = "identification")
    @Mapping(source = "entity.adress", target = "adress")
    @Mapping(source = "entity.phone", target = "phone")
    @Mapping(source = "entity.personId", target = "customerId")
    @Mapping(source = "entity.password", target = "password")
    @Mapping(source = "entity.status", target = "status")
    CustomerResponseDto entityToResponse(CustomerEntity entity);

    @Mapping(source = "request.name", target = "name")
    @Mapping(source = "request.gender", target = "gender")
    @Mapping(source = "request.age", target = "age")
    @Mapping(source = "request.identification", target = "identification")
    @Mapping(source = "request.adress", target = "adress")
    @Mapping(source = "request.phone", target = "phone")
    @Mapping(source = "request.password", target = "password")
    @Mapping(source = "request.status", target = "status")
    CustomerEntity requestToEntity(CustomerCreateRequestDto request);
}
