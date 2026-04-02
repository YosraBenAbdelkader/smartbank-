package com.banking.smartbank.dto.request;

import com.banking.smartbank.domain.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class CreateAccountRequest {
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code (ex: EUR, USD)")
    private String currency;

    @NotNull(message = "Account type is required")
    private AccountType type;
}
