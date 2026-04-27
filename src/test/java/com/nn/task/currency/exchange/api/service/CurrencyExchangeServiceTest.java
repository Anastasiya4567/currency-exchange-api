package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.domain.model.AccountBalances;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.exception.UnsupportedCurrencyException;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceTest {

    private static final BigDecimal EXPECTED_FROM_BALANCE = new BigDecimal("90.00");
    private static final BigDecimal EXPECTED_TO_BALANCE = new BigDecimal("52.50");

    @Mock
    private ExchangeValidator exchangeValidator;

    @Mock
    private AccountBalanceService accountBalanceService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private CurrencyExchangeService currencyExchangeService;

    private Account account;

    private UUID accountId;

    private ExchangeRequest exchangeRequest;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setBalancePLN(new BigDecimal(BALANCE_PLN));
        account.setBalanceUSD(new BigDecimal(BALANCE_USD));
        accountId = UUID.randomUUID();
        exchangeRequest = new ExchangeRequest();
        exchangeRequest.setAmount(AMOUNT_10);
        exchangeRequest.setFromCurrency(FROM_CURRENCY);
        exchangeRequest.setToCurrency(TO_CURRENCY);
    }

    @Test
    void exchangeCurrency_shouldCallDependencies() {
        // given
        when(accountService.findAccount(accountId)).thenReturn(account);
        AccountBalances balances = new AccountBalances(FROM_CURRENCY, EXPECTED_FROM_BALANCE, TO_CURRENCY, EXPECTED_TO_BALANCE);
        when(accountBalanceService.calculateNewBalances(account, FROM_CURRENCY, TO_CURRENCY, new BigDecimal(AMOUNT_10))).thenReturn(balances);

        // when
        currencyExchangeService.exchangeCurrency(accountId, exchangeRequest);

        // then
        verify(exchangeValidator).validateAll(account, exchangeRequest);
        verify(accountBalanceService).calculateNewBalances(account, FROM_CURRENCY, TO_CURRENCY, new BigDecimal(AMOUNT_10));
        verify(accountBalanceService).setAccountBalances(account, balances);
        verify(accountService).saveAccount(account);
    }

    @Test
    void determineTargetCurrency_shouldThrowForUnsupported() {
        // given
        ExchangeRequest request = new ExchangeRequest();
        request.setAmount(AMOUNT_10);
        request.setFromCurrency(UNSUPPORTED_CURRENCY);

        // when / then
        assertThrows(UnsupportedCurrencyException.class, () ->
            currencyExchangeService.exchangeCurrency(accountId, request)
        );
    }
}