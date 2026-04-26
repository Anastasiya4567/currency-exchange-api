package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;

@Component
public class ExchangeValidator {

    public void validateAll(Account account, ExchangeRequest exchangeRequest) {
        validateExchangeRequest(exchangeRequest);
        BigDecimal amount = new BigDecimal(exchangeRequest.getAmount());
        String fromCurrency = exchangeRequest.getFromCurrency();
        validateSufficientBalance(account, fromCurrency, amount);
    }

    public void validateExchangeInputs(UUID accountId, ExchangeRequest exchangeRequest) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID must not be null");
        }
        if (exchangeRequest == null) {
            throw new IllegalArgumentException("Exchange request must not be null");
        }
    }

    private void validateExchangeRequest(ExchangeRequest exchangeRequest) {
        String amountRaw = exchangeRequest.getAmount();
        if (amountRaw == null) {
            throw new IllegalArgumentException("Amount must be provided");
        }
        BigDecimal amount = new BigDecimal(amountRaw);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        String from = exchangeRequest.getFromCurrency();
        if (from == null) {
            throw new IllegalArgumentException("fromCurrency must be provided");
        }
    }

    private void validateSufficientBalance(Account account, String from, BigDecimal amount) {
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

