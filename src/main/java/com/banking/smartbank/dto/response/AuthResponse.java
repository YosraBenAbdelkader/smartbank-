package com.banking.smartbank.dto.response;

public record AuthResponse(
        String token,
        String email,
        String role
) {}