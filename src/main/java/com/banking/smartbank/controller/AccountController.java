package com.banking.smartbank.controller;

import com.banking.smartbank.dto.request.CreateAccountRequest;
import com.banking.smartbank.dto.response.AccountResponse;
import com.banking.smartbank.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<AccountResponse> createAccount(@PathVariable Long userId,
                                                         @RequestBody @Valid CreateAccountRequest createAccountRequest) {
        return new ResponseEntity<>(accountService.createAccount(userId, createAccountRequest), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.findByUserId(userId));
    }
}
