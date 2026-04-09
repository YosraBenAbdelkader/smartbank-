package com.banking.smartbank.dto.response;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String role
) {}