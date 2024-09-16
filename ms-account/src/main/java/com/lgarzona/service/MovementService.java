package com.lgarzona.service;

import com.lgarzona.service.dto.MovementCreateRequestDto;
import com.lgarzona.service.dto.MovementResponseDto;

public interface MovementService {
    MovementResponseDto create(MovementCreateRequestDto movementRequest);
}
