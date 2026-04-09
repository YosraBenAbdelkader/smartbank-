package com.banking.smartbank.service.impl;

import com.banking.smartbank.domain.Transfer;
import com.banking.smartbank.domain.enums.TransferStatus;
import com.banking.smartbank.dto.response.TransferResponse;
import com.banking.smartbank.exception.InvalidTransferStateException;
import com.banking.smartbank.exception.ResourceNotFoundException;
import com.banking.smartbank.mapper.TransferMapper;
import com.banking.smartbank.repository.TransferRepository;
import com.banking.smartbank.service.TransferWorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferWorkflowServiceImpl implements TransferWorkflowService {

    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;

    @Override
    @Transactional
    public TransferResponse validate(Long transferId) {
        Transfer transfer = findTransfer(transferId);
        assertStatus(transfer, TransferStatus.PENDING);
        transfer.setStatus(TransferStatus.VALIDATED);
        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    @Override
    @Transactional
    public TransferResponse reject(Long transferId, String reason) {
        Transfer transfer = findTransfer(transferId);
        assertStatus(transfer, TransferStatus.PENDING);
        transfer.setStatus(TransferStatus.CANCELLED);
        transfer.setReason(reason);
        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    @Override
    @Transactional
    public TransferResponse process(Long transferId) {
        Transfer transfer = findTransfer(transferId);
        assertStatus(transfer, TransferStatus.VALIDATED);
        transfer.setStatus(TransferStatus.PROCESSING);
        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    @Override
    @Transactional
    public TransferResponse complete(Long transferId) {
        Transfer transfer = findTransfer(transferId);
        assertStatus(transfer, TransferStatus.PROCESSING);
        transfer.setStatus(TransferStatus.COMPLETED);
        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    private Transfer findTransfer(Long transferId) {
        return transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id: " + transferId));
    }

    private void assertStatus(Transfer transfer, TransferStatus expected) {
        if (transfer.getStatus() != expected) {
            throw new InvalidTransferStateException(switch (expected) {
                case PENDING -> "Transfer must be PENDING to be validated or rejected";
                case VALIDATED -> "Transfer must be VALIDATED to be processed";
                case PROCESSING -> "Transfer must be PROCESSING to be completed";
                default -> "Invalid transfer state: " + transfer.getStatus();
            });
        }
    }
}