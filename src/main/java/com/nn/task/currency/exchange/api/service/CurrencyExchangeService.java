package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.domain.model.AccountBalances;
import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.exception.AccountNotFoundException;
import com.nn.task.currency.exchange.api.exception.UnsupportedCurrencyException;
import com.nn.task.currency.exchange.api.mapper.AccountEntityMapper;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import com.nn.task.currency.exchange.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {
    private final AccountRepository accountRepository;
    private final ExchangeValidator exchangeValidator;
    private final AccountEntityMapper accountEntityMapper;
    private final AccountBalanceService accountBalanceService;

    private static final Map<String, String> DEFAULT_TARGET_CURRENCY = Map.of(
        PLN.name(), USD.name(),
        USD.name(), PLN.name()
    );

    public AccountDetails exchangeCurrency(UUID accountId, ExchangeRequest exchangeRequest) {
        Account account = performExchange(accountId, exchangeRequest);
        return accountEntityMapper.toAccountDetails(account);
    }

    @Transactional
    public Account performExchange(UUID accountId, ExchangeRequest exchangeRequest) {
        exchangeValidator.validateExchangeInputs(accountId, exchangeRequest);
        var account = getAccountOrThrow(accountId);
        exchangeValidator.validateAll(account, exchangeRequest);
        var amount = new BigDecimal(exchangeRequest.getAmount());
        var fromCurrency = exchangeRequest.getFromCurrency();
        var toCurrency = determineTargetCurrency(fromCurrency, exchangeRequest.getToCurrency());
        AccountBalances newBalances = accountBalanceService.calculateNewBalances(account, fromCurrency, toCurrency, amount);
        accountBalanceService.setAccountBalances(account, newBalances);
        return accountRepository.save(account);
    }

    private String determineTargetCurrency(String fromCurrency, String toCurrency) {
        if (fromCurrency == null || fromCurrency.isBlank()) {
            throw new IllegalArgumentException("Source currency must not be null or blank");
        }
        if (toCurrency == null || toCurrency.isBlank() || toCurrency.equalsIgnoreCase(fromCurrency)) {
            String defaultTarget = DEFAULT_TARGET_CURRENCY.get(fromCurrency.toUpperCase());
            if (defaultTarget == null) {
                throw new UnsupportedCurrencyException(fromCurrency);
            }
            return defaultTarget;
        }
        return toCurrency;
    }

    private Account getAccountOrThrow(UUID id) {
        return accountRepository.findById(id)
            .orElseThrow(AccountNotFoundException::new);
    }

}
