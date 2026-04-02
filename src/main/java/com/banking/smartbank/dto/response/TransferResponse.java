package com.banking.smartbank.dto.response;

import com.banking.smartbank.domain.enums.TransferStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransferResponse {
    private Long id;
    private BigDecimal amount;
    private TransferStatus status;
    private String reason;
    private LocalDateTime createdAt;
}
