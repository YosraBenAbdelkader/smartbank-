package com.banking.smartbank.exception;

public final class ResourceNotFoundException extends BankingException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}