package com.banking.smartbank.exception;

public sealed class BankingException extends RuntimeException
        permits ResourceNotFoundException, InsufficientFundsException, InvalidTransferStateException {

    protected BankingException(String message) {
        super(message);
    }
}