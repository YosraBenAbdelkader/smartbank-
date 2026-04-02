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
        Transfer transfer= transferRepository.findById(transferId).orElseThrow(()->
                new RuntimeException("Transfer not found"));

        if(transfer.getStatus()!= TransferStatus.PENDING){
            throw new InvalidTransferStateException("Transfer must be PENDING to be validated");
        }
        transfer.setStatus(TransferStatus.VALIDATED);

        Transfer savedTransfer=transferRepository.save(transfer);
        return transferMapper.toResponse(savedTransfer);
    }

    @Override
    @Transactional
    public TransferResponse reject(Long transferId, String reason) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id: " + transferId));

        if (transfer.getStatus() != TransferStatus.PENDING) {
            throw new InvalidTransferStateException("Transfer must be PENDING to be rejected");
        }

        transfer.setStatus(TransferStatus.CANCELLED);
        transfer.setReason(reason);
        Transfer savedTransfer = transferRepository.save(transfer);
        return transferMapper.toResponse(savedTransfer);
    }

    @Override
    @Transactional
    public TransferResponse process(Long transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id: " + transferId));

        if (transfer.getStatus() != TransferStatus.VALIDATED) {
            throw new InvalidTransferStateException("Transfer must be VALIDATED to be processed");
        }

        transfer.setStatus(TransferStatus.PROCESSING);
        Transfer savedTransfer = transferRepository.save(transfer);
        return transferMapper.toResponse(savedTransfer);
    }

    @Override
    @Transactional
    public TransferResponse complete(Long transferId) {
        Transfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException("Transfer not found with id: " + transferId));

        if (transfer.getStatus() != TransferStatus.PROCESSING) {
            throw new InvalidTransferStateException("Transfer must be PROCESSING to be completed");
        }

        transfer.setStatus(TransferStatus.COMPLETED);
        Transfer savedTransfer = transferRepository.save(transfer);
        return transferMapper.toResponse(savedTransfer);
    }
}
