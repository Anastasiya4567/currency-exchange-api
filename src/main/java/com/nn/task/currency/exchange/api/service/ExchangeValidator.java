package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;

@Component
public class ExchangeValidator {
    public void validateExchangeRequest(ExchangeRequest exchangeRequest) {
        Double amountRaw = exchangeRequest.getAmount();
        if (amountRaw == null || amountRaw <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        String from = exchangeRequest.getFromCurrency();
        if (from == null) {
            throw new IllegalArgumentException("fromCurrency must be provided");
        }
    }

    public void validateSufficientBalance(Account account, String from, BigDecimal amount) {
        if (PLN.name().equalsIgnoreCase(from)) {
            if (account.getBalancePLN().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient PLN balance");
            }
        } else if (USD.name().equalsIgnoreCase(from)) {
            if (account.getBalanceUSD().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient USD balance");
            }
        } else {
            throw new IllegalArgumentException("Unsupported currency: " + from);
        }
    }
}

