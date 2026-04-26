package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.domain.model.AccountBalances;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.exception.UnsupportedCurrencyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;

@Service
@RequiredArgsConstructor
public class AccountBalanceService {

    private final CurrencyConversionService conversionService;

    private static final Map<String, java.util.function.Function<Account, BigDecimal>> BALANCE_GETTERS = Map.of(
        PLN.name(), Account::getBalancePLN,
        USD.name(), Account::getBalanceUSD
    );

    private static final Map<String, BiConsumer<Account, BigDecimal>> BALANCE_SETTERS = Map.of(
        PLN.name(), Account::setBalancePLN,
        USD.name(), Account::setBalanceUSD
    );

    public AccountBalances calculateNewBalances(Account account, String fromCurrency, String toCurrency, BigDecimal amount) {
        var fromCurrencyBalance = getBalance(account, fromCurrency);
        var toCurrencyBalance = getBalance(account, toCurrency);
        var exchanged = conversionService.exchange(amount, fromCurrency, toCurrency);
        fromCurrencyBalance = fromCurrencyBalance.subtract(amount);
        toCurrencyBalance = toCurrencyBalance.add(exchanged);
        return new AccountBalances(fromCurrency, fromCurrencyBalance, toCurrency, toCurrencyBalance);
    }

    public void setAccountBalances(Account account, AccountBalances balances) {
        setBalance(account, balances.fromCurrency(), balances.fromBalance());
        setBalance(account, balances.toCurrency(), balances.toBalance());
    }

    private BigDecimal getBalance(Account account, String currency) {
        var balanceGetter = BALANCE_GETTERS.get(currency.toUpperCase());
        if (balanceGetter == null) {
            throw new UnsupportedCurrencyException(currency);
        }
        return balanceGetter.apply(account);
    }

    private void setBalance(Account account, String currency, BigDecimal value) {
        var balanceSetter = BALANCE_SETTERS.get(currency.toUpperCase());
        if (balanceSetter == null) {
            throw new UnsupportedCurrencyException(currency);
        }
        balanceSetter.accept(account, value);
    }
}
