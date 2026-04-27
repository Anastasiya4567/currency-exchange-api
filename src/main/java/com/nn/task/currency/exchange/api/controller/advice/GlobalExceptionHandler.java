package com.nn.task.currency.exchange.api.controller.advice;

import com.nn.task.currency.exchange.api.exception.AccountNotFoundException;
import com.nn.task.currency.exchange.api.exception.NegativeAmountException;
import com.nn.task.currency.exchange.api.exception.UnsupportedCurrencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        return buildErrorResponse("INTERNAL_ERROR", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR, ex);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiError> handleAccountNotFound(AccountNotFoundException ex) {
        return buildErrorResponse("ACCOUNT_NOT_FOUND", ex.getMessage(), HttpStatus.BAD_REQUEST, LogLevel.WARN, ex);
    }

    @ExceptionHandler(UnsupportedCurrencyException.class)
    public ResponseEntity<ApiError> handleUnsupportedCurrency(UnsupportedCurrencyException ex) {
        return buildErrorResponse("UNSUPPORTED_CURRENCY", ex.getMessage(), HttpStatus.BAD_REQUEST, LogLevel.WARN, ex);
    }

    @ExceptionHandler(NegativeAmountException.class)
    public ResponseEntity<ApiError> handleNegativeAmount(NegativeAmountException ex) {
        return buildErrorResponse("NEGATIVE_AMOUNT", ex.getMessage(), HttpStatus.BAD_REQUEST, LogLevel.WARN, ex);
    }

    private enum LogLevel {ERROR, WARN}

    private ResponseEntity<ApiError> buildErrorResponse(String code, String message, HttpStatus status, LogLevel logLevel, Exception ex) {
        switch (logLevel) {
            case ERROR -> log.error("{}: {}", code, message, ex);
            case WARN -> log.warn("{}: {}", code, message);
        }
        ApiErrorResponse body = new ApiErrorResponse(code, message, LocalDateTime.now());
        return ResponseEntity.status(status).body(body);
    }
}
