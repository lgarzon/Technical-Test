package com.lgarzona.service.mapper;

import com.lgarzona.domain.MovementEntity;
import com.lgarzona.service.dto.MovementCreateRequestDto;
import com.lgarzona.service.dto.MovementResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MovementMapper {

    @Mapping(source = "entity.type", target = "type")
    @Mapping(source = "entity.amount", target = "amount")
    @Mapping(source = "entity.balance", target = "balance")
    @Mapping(source = "entity.creationDate", target = "date")
    @Mapping(source = "entity.account.accountId", target = "accountId")
    MovementResponseDto entityToResponse(MovementEntity entity);

    @Mapping(source = "request.type", target = "type")
    @Mapping(source = "request.amount", target = "amount")
    @Mapping(source = "request.accountId", target = "account.accountId")
    MovementEntity requestToEntity(MovementCreateRequestDto request);
}
