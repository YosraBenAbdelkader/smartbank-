package com.banking.smartbank.service;

import com.banking.smartbank.dto.response.TransferResponse;

public interface TransferWorkflowService {
    TransferResponse validate(Long transferId);
    TransferResponse reject(Long transferId, String reason);
    TransferResponse process(Long transferId);
    TransferResponse complete(Long transferId);
}
