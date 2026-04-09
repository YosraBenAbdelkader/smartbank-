package com.banking.smartbank.service;

import com.banking.smartbank.domain.Account;
import com.banking.smartbank.domain.User;
import com.banking.smartbank.domain.enums.AccountType;
import com.banking.smartbank.domain.enums.Role;
import com.banking.smartbank.dto.request.CreateAccountRequest;
import com.banking.smartbank.dto.response.AccountResponse;
import com.banking.smartbank.exception.ResourceNotFoundException;
import com.banking.smartbank.mapper.AccountMapper;
import com.banking.smartbank.repository.AccountRepository;
import com.banking.smartbank.repository.UserRepository;
import com.banking.smartbank.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;
    private Account account;
    private AccountResponse accountResponse;
    private CreateAccountRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("yosra@smartbank.com")
                .role(Role.CLIENT)
                .build();

        account = Account.builder()
                .id(1L)
                .user(user)
                .accountNumber("SB123456")
                .type("CHECKING")
                .balance(BigDecimal.ZERO)
                .currency("EUR")
                .build();

        accountResponse = new AccountResponse(1L, "SB123456", "CHECKING", BigDecimal.ZERO, "EUR");
        request = new CreateAccountRequest("EUR", AccountType.CHECKING);
    }

    @Test
    void createAccount_ShouldReturnAccount_WhenUserExists() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        // WHEN
        AccountResponse result = accountService.createAccount(1L, request);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.accountNumber()).isEqualTo("SB123456");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_ShouldThrowException_WhenUserNotFound() {
        // GIVEN
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThatThrownBy(() -> accountService.createAccount(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findById_ShouldReturnAccount_WhenAccountExists() {
        // GIVEN
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        // WHEN
        AccountResponse result = accountService.findById(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenAccountNotFound() {
        // GIVEN
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThatThrownBy(() -> accountService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findByUserId_ShouldReturnAccounts() {
        // GIVEN
        when(accountRepository.findByUserId(1L)).thenReturn(List.of(account));
        when(accountMapper.toResponse(account)).thenReturn(accountResponse);

        // WHEN
        List<AccountResponse> results = accountService.findByUserId(1L);

        // THEN
        assertThat(results).hasSize(1);
        verify(accountRepository, times(1)).findByUserId(1L);
    }
}