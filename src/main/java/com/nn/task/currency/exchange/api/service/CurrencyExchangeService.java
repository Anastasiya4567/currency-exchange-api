package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.constants.CurrencyExchangeConstants;
import com.nn.task.currency.exchange.api.domain.model.AccountBalances;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.exception.UnsupportedCurrencyException;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {
    private final ExchangeValidator exchangeValidator;
    private final AccountBalanceService accountBalanceService;
    private final AccountService accountService;

    @Transactional
    public void exchangeCurrency(UUID accountId, ExchangeRequest exchangeRequest) {
        var account = accountService.findAccount(accountId);
        exchangeValidator.validateAll(account, exchangeRequest);
        var amount = new BigDecimal(exchangeRequest.getAmount());
        var fromCurrency = exchangeRequest.getFromCurrency();
        var toCurrency = determineTargetCurrency(fromCurrency, exchangeRequest.getToCurrency());
        applyBalances(account, fromCurrency, toCurrency, amount);
        accountService.saveAccount(account);
    }

    private String determineTargetCurrency(String fromCurrency, String toCurrency) {
        if (toCurrency == null || toCurrency.isBlank() || toCurrency.equalsIgnoreCase(fromCurrency)) {
            String defaultTarget = CurrencyExchangeConstants.DEFAULT_TARGET_CURRENCY.get(fromCurrency.toUpperCase());
            if (defaultTarget == null) {
                throw new UnsupportedCurrencyException(fromCurrency);
            }
            return defaultTarget;
        }
        return toCurrency;
    }

    private void applyBalances(Account account, String fromCurrency, String toCurrency, BigDecimal amount) {
        AccountBalances newBalances = accountBalanceService.calculateNewBalances(account, fromCurrency, toCurrency, amount);
        accountBalanceService.setAccountBalances(account, newBalances);
    }

}
