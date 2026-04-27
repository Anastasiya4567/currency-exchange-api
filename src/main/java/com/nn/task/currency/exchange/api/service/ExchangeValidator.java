package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.exception.UnsupportedCurrencyException;
import com.nn.task.currency.exchange.api.exception.NegativeAmountException;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;

@Component
public class ExchangeValidator {

    public void validateAll(Account account, ExchangeRequest exchangeRequest) {
        BigDecimal amount = new BigDecimal(exchangeRequest.getAmount());
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeAmountException();
        }
        String fromCurrency = exchangeRequest.getFromCurrency();
        validateSufficientBalance(account, fromCurrency, amount);
    }

    private void validateSufficientBalance(Account account, String fromCurrency, BigDecimal amount) {
        if (PLN.name().equalsIgnoreCase(fromCurrency)) {
            if (account.getBalancePLN().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient PLN balance");
            }
        } else if (USD.name().equalsIgnoreCase(fromCurrency)) {
            if (account.getBalanceUSD().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient USD balance");
            }
        } else {
            throw new UnsupportedCurrencyException(fromCurrency);
        }
    }
}
