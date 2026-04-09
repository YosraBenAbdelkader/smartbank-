package com.banking.smartbank.dto.response;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String accountNumber,
        String type,
        BigDecimal balance,
        String currency
) {}