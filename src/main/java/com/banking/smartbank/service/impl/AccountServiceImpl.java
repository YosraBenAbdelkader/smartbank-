package com.banking.smartbank.service.impl;

import com.banking.smartbank.domain.Account;
import com.banking.smartbank.domain.User;
import com.banking.smartbank.dto.request.CreateAccountRequest;
import com.banking.smartbank.dto.response.AccountResponse;
import com.banking.smartbank.exception.ResourceNotFoundException;
import com.banking.smartbank.mapper.AccountMapper;
import com.banking.smartbank.repository.AccountRepository;
import com.banking.smartbank.repository.UserRepository;
import com.banking.smartbank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountResponse createAccount(Long userId, CreateAccountRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id " + userId));

        Account account = Account.builder()
                .user(user)
                .accountNumber(generateAccountNumber())
                .type(request.type().name())
                .balance(BigDecimal.ZERO)
                .currency(request.currency())
                .build();

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toResponse(savedAccount);
    }

    @Override
    public AccountResponse findById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Account not found with id " + id));
        return accountMapper.toResponse(account);
    }

    @Override
    public List<AccountResponse> findByUserId(Long userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    private String generateAccountNumber() {
        return "SB" + System.currentTimeMillis();
    }
}