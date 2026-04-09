package com.banking.smartbank.service.impl;

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
import com.banking.smartbank.service.AccountService;
import com.banking.smartbank.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;

    @Override
    public TransferResponse createTransfer(Long senderAccountId, CreateTransferRequest request) {
        Account senderAccount = accountRepository.findById(senderAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + senderAccountId));

        Account receiverAccount = accountRepository.findByAccountNumber(request.receiverAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + request.receiverAccountNumber()));

        if (senderAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        Transfer transfer = Transfer.builder()
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .amount(request.amount())
                .status(TransferStatus.PENDING)
                .reason(request.reason())
                .build();

        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    @Override
    public TransferResponse findById(Long id) {
        Transfer transfer = transferRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Transfer not found with id: " + id));
        return transferMapper.toResponse(transfer);
    }

    @Override
    public List<TransferResponse> findBySenderAccountId(Long accountId) {
        return transferRepository.findBySenderAccountId(accountId)
                .stream()
                .map(transferMapper::toResponse)
                .toList();
    }
}