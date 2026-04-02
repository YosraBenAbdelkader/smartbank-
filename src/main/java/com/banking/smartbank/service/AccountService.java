package com.banking.smartbank.service;

import com.banking.smartbank.dto.request.CreateAccountRequest;
import com.banking.smartbank.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(Long userId, CreateAccountRequest request);
    AccountResponse findById(Long id);
    List<AccountResponse> findByUserId(Long userId);
}
