package com.banking.smartbank.service;

import com.banking.smartbank.domain.Account;
import com.banking.smartbank.domain.Transfer;
import com.banking.smartbank.domain.enums.TransferStatus;
import com.banking.smartbank.dto.request.CreateTransferRequest;
import com.banking.smartbank.dto.response.TransferResponse;
import com.banking.smartbank.exception.InsufficientFundsException;
import com.banking.smartbank.exception.ResourceNotFoundException;
import com.banking.smartbank.mapper.TransferMapper;
import com.banking.smartbank.repository.AccountRepository;
import com.banking.smartbank.repository.TransferRepository;
import com.banking.smartbank.service.impl.TransferServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferMapper transferMapper;

    @InjectMocks
    private TransferServiceImpl transferService;

    private Account senderAccount;
    private Account receiverAccount;
    private Transfer transfer;
    private TransferResponse transferResponse;
    private CreateTransferRequest request;

    @BeforeEach
    void setUp() {
        senderAccount = Account.builder()
                .id(1L)
                .accountNumber("SB001")
                .balance(new BigDecimal("1000.00"))
                .currency("EUR")
                .build();

        receiverAccount = Account.builder()
                .id(2L)
                .accountNumber("SB002")
                .balance(new BigDecimal("500.00"))
                .currency("EUR")
                .build();

        transfer = Transfer.builder()
                .id(1L)
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .amount(new BigDecimal("100.00"))
                .status(TransferStatus.PENDING)
                .reason("Test transfer")
                .build();

        transferResponse = new TransferResponse(1L, new BigDecimal("100.00"), TransferStatus.PENDING, null, null);
        request = new CreateTransferRequest("SB002", new BigDecimal("100.00"), "Test transfer");
    }

    @Test
    void createTransfer_ShouldReturnTransfer_WhenValid() {
        // GIVEN
        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountNumber("SB002")).thenReturn(Optional.of(receiverAccount));
        when(transferRepository.save(any(Transfer.class))).thenReturn(transfer);
        when(transferMapper.toResponse(transfer)).thenReturn(transferResponse);

        // WHEN
        TransferResponse result = transferService.createTransfer(1L, request);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.amount()).isEqualTo(new BigDecimal("100.00"));
        verify(transferRepository, times(1)).save(any(Transfer.class));
    }

    @Test
    void createTransfer_ShouldThrowException_WhenInsufficientFunds() {
        // GIVEN
        CreateTransferRequest bigRequest = new CreateTransferRequest("SB002", new BigDecimal("9999.00"), "Test transfer");
        when(accountRepository.findById(1L)).thenReturn(Optional.of(senderAccount));
        when(accountRepository.findByAccountNumber("SB002")).thenReturn(Optional.of(receiverAccount));

        // WHEN + THEN
        assertThatThrownBy(() -> transferService.createTransfer(1L, bigRequest))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void createTransfer_ShouldThrowException_WhenSenderNotFound() {
        // GIVEN
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThatThrownBy(() -> transferService.createTransfer(99L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findById_ShouldReturnTransfer_WhenExists() {
        // GIVEN
        when(transferRepository.findById(1L)).thenReturn(Optional.of(transfer));
        when(transferMapper.toResponse(transfer)).thenReturn(transferResponse);

        // WHEN
        TransferResponse result = transferService.findById(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        // GIVEN
        when(transferRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThatThrownBy(() -> transferService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findBySenderAccountId_ShouldReturnList() {
        // GIVEN
        when(transferRepository.findBySenderAccountId(1L)).thenReturn(List.of(transfer));
        when(transferMapper.toResponse(transfer)).thenReturn(transferResponse);

        // WHEN
        List<TransferResponse> results = transferService.findBySenderAccountId(1L);

        // THEN
        assertThat(results).asList().hasSize(1);
        verify(transferRepository, times(1)).findBySenderAccountId(1L);
    }
}