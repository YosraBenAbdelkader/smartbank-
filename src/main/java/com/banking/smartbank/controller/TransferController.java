package com.banking.smartbank.controller;

import com.banking.smartbank.dto.request.CreateTransferRequest;
import com.banking.smartbank.dto.response.TransferResponse;
import com.banking.smartbank.exception.InsufficientFundsException;
import com.banking.smartbank.service.TransferService;
import com.banking.smartbank.service.TransferWorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final TransferWorkflowService transferWorkflowService;

    @PostMapping("/account/{accountId}")
    public ResponseEntity<TransferResponse> createTransfer(
            @PathVariable Long accountId,
            @RequestBody @Valid CreateTransferRequest request) throws InsufficientFundsException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transferService.createTransfer(accountId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getTransfer(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.findById(id));
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<TransferResponse> validate(@PathVariable Long id) {
        return ResponseEntity.ok(transferWorkflowService.validate(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<TransferResponse> reject(
            @PathVariable Long id,
            @RequestParam String reason) {
        return ResponseEntity.ok(transferWorkflowService.reject(id, reason));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<TransferResponse> process(@PathVariable Long id) {
        return ResponseEntity.ok(transferWorkflowService.process(id));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<TransferResponse> complete(@PathVariable Long id) {
        return ResponseEntity.ok(transferWorkflowService.complete(id));
    }
}
