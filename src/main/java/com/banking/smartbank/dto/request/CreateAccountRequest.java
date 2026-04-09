package com.banking.smartbank.dto.request;

import com.banking.smartbank.domain.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAccountRequest(
        @NotBlank(message = "Currency is required")
        @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code (ex: EUR, USD)")
        String currency,

        @NotNull(message = "Account type is required")
        AccountType type
) {}