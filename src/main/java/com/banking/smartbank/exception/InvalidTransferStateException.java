package com.banking.smartbank.exception;

public final class InvalidTransferStateException extends BankingException {
    public InvalidTransferStateException(String message) {
        super(message);
    }
}