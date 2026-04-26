package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.client.NbpClient;
import com.nn.task.currency.exchange.api.domain.model.AccountBalances;
import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.mapper.AccountDetailsMapper;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import com.nn.task.currency.exchange.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {
    private final NbpClient nbpClient;
    private final AccountRepository accountRepository;
    private final ExchangeValidator exchangeValidator;
    private final AccountDetailsMapper accountDetailsMapper;
    private static final Map<String, java.util.function.Function<Account, BigDecimal>> BALANCE_GETTERS = Map.of(
        PLN.name(), Account::getBalancePLN,
        USD.name(), Account::getBalanceUSD
    );

    private static final Map<String, BiConsumer<Account, BigDecimal>> BALANCE_SETTERS = Map.of(
        PLN.name(), Account::setBalancePLN,
        USD.name(), Account::setBalanceUSD
    );

    public AccountDetails exchangeCurrency(UUID accountId, ExchangeRequest exchangeRequest) {
        Account account = performExchange(accountId, exchangeRequest);
        return accountDetailsMapper.toAccountDetails(account);
    }

    @Transactional
    public Account performExchange(UUID accountId, ExchangeRequest exchangeRequest) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID must not be null");
        }
        if (exchangeRequest == null) {
            throw new IllegalArgumentException("Exchange request must not be null");
        }
        var account = getAccountOrThrow(accountId);
        exchangeValidator.validateExchangeRequest(exchangeRequest);
        BigDecimal amount = BigDecimal.valueOf(exchangeRequest.getAmount());
        var fromCurrency = exchangeRequest.getFromCurrency();
        exchangeValidator.validateSufficientBalance(account, fromCurrency, amount);
        var toCurrency = determineTargetCurrency(fromCurrency, exchangeRequest.getToCurrency());
        AccountBalances newBalances = calculateNewBalances(account, fromCurrency, toCurrency, amount);
        setAccountBalances(account, newBalances);
        return accountRepository.save(account);
    }

    private String determineTargetCurrency(String fromCurrency, String toCurrency) {
        if (fromCurrency == null || fromCurrency.isBlank()) {
            throw new IllegalArgumentException("Source currency must not be null or blank");
        }
        if (toCurrency == null || toCurrency.isBlank() || toCurrency.equalsIgnoreCase(fromCurrency)) {
            if (PLN.name().equalsIgnoreCase(fromCurrency)) {
                return USD.name();
            } else if (USD.name().equalsIgnoreCase(fromCurrency)) {
                return PLN.name();
            } else {
                throw new IllegalArgumentException("Unsupported currency: " + fromCurrency);
            }
        }
        return toCurrency;
    }

    private Account getAccountOrThrow(UUID id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    private AccountBalances calculateNewBalances(Account account, String from, String to, BigDecimal amount) {
        BigDecimal fromBalance = getBalance(account, from);
        BigDecimal toBalance = getBalance(account, to);
        BigDecimal exchanged = exchange(amount, from, to);
        fromBalance = fromBalance.subtract(amount);
        toBalance = toBalance.add(exchanged);
        return new AccountBalances(from, fromBalance, to, toBalance);
    }

    private BigDecimal getBalance(Account account, String currency) {
        var getter = BALANCE_GETTERS.get(currency.toUpperCase());
        if (getter == null) {
            throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
        return getter.apply(account);
    }

    private void setAccountBalances(Account account, AccountBalances balances) {
        setBalance(account, balances.fromCurrency(), balances.fromBalance());
        setBalance(account, balances.toCurrency(), balances.toBalance());
    }

    private void setBalance(Account account, String currency, BigDecimal value) {
        var setter = BALANCE_SETTERS.get(currency.toUpperCase());
        if (setter == null) {
            throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
        setter.accept(account, value);
    }

    public BigDecimal exchange(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return amount;
        }
        try {
            if (PLN.name().equalsIgnoreCase(fromCurrency) && USD.name().equalsIgnoreCase(toCurrency)) {
                BigDecimal rate = getUsdPlnRate();
                return amount.divide(rate, 4, RoundingMode.HALF_UP);
            } else if (USD.name().equalsIgnoreCase(fromCurrency) && PLN.name().equalsIgnoreCase(toCurrency)) {
                BigDecimal rate = getUsdPlnRate();
                return amount.multiply(rate);
            }
        } catch (Exception e) {
            // fallback to 1:1 if API fails
            return amount;
        }
        return amount;
    }

    private BigDecimal getUsdPlnRate() {
        try {
            var response = nbpClient.getUsdPlnRate();
            if (response != null && response.getRates() != null && !response.getRates().isEmpty()) {
                double mid = response.getRates().getFirst().getMid();
                return BigDecimal.valueOf(mid);
            }
        } catch (Exception e) {
            // ignore and fallback
        }
        return BigDecimal.ONE;
    }
}
