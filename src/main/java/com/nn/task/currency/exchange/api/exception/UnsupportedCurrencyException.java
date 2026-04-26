package com.nn.task.currency.exchange.api.exception;

public class UnsupportedCurrencyException extends RuntimeException {
    public UnsupportedCurrencyException(String currency) {
        super("Unsupported currency: " + currency);
    }
}