package com.banking.smartbank.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String type;
    private BigDecimal balance;
    private String currency;
}
