package com.nn.task.currency.exchange.api.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String currency) {
        super("Insufficient " + currency + " balance");
    }
}