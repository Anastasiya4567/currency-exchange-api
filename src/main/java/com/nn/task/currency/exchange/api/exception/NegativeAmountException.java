package com.nn.task.currency.exchange.api.exception;

public class NegativeAmountException extends RuntimeException {
    public NegativeAmountException() {
        super("Amount must be positive");
    }

    public NegativeAmountException(String message) {
        super(message);
    }
}
