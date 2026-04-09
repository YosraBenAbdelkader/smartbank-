package com.banking.smartbank.dto.response;

import com.banking.smartbank.domain.enums.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        Long id,
        BigDecimal amount,
        TransferStatus status,
        String reason,
        LocalDateTime createdAt
) {}