package com.banking.smartbank.service;

import com.banking.smartbank.dto.request.LoginRequest;
import com.banking.smartbank.dto.request.RegisterRequest;
import com.banking.smartbank.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
