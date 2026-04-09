package com.banking.smartbank.service;

import com.banking.smartbank.dto.request.CreateTransferRequest;
import com.banking.smartbank.dto.response.TransferResponse;

import java.util.List;

public interface TransferService {
    TransferResponse createTransfer(Long senderAccountId, CreateTransferRequest request);
    TransferResponse findById(Long id);
    List<TransferResponse> findBySenderAccountId(Long accountId);
}