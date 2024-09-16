package com.lgarzona.service.mapper;

import com.lgarzona.domain.AccountEntity;
import com.lgarzona.service.dto.AccountCreateRequestDto;
import com.lgarzona.service.dto.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    @Mapping(source = "entity.accountId", target = "accountId")
    @Mapping(source = "entity.accountNumber", target = "accountNumber")
    @Mapping(source = "entity.accountType", target = "accountType")
    @Mapping(source = "entity.balance", target = "balance")
    @Mapping(source = "entity.status", target = "status")
    @Mapping(source = "entity.customerId", target = "customerId")
    AccountResponseDto entityToResponse(AccountEntity entity);


    @Mapping(source = "request.accountNumber", target = "accountNumber")
    @Mapping(source = "request.accountType", target = "accountType")
    @Mapping(source = "request.balance", target = "balance")
    @Mapping(source = "request.status", target = "status")
    @Mapping(source = "request.customerId", target = "customerId")
    AccountEntity requestToEntity(AccountCreateRequestDto request);
}
