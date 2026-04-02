package com.banking.smartbank.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransferRequest {

    @NotBlank(message = "Receiver account number is required")
    private String receiverAccountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 17, fraction = 2)     // aligné avec DECIMAL(19,2)
    private BigDecimal amount;

    @Size(max = 255, message = "Reason must not exceed 255 characters")
    private String reason;
}
