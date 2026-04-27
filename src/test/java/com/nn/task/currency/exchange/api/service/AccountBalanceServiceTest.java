package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.domain.model.AccountBalances;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.exception.UnsupportedCurrencyException;
import com.nn.task.currency.exchange.api.testutil.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountBalanceServiceTest {

    private static final BigDecimal EXPECTED_FROM_BALANCE = new BigDecimal("90.00");
    private static final BigDecimal EXPECTED_TO_BALANCE = new BigDecimal("52.50");
    private static final BigDecimal BALANCE_PLN = new BigDecimal("80.00");
    private static final BigDecimal BALANCE_USD = new BigDecimal("60.00");
    private static final BigDecimal EXPECTED_CONVERTED_AMOUNT = new BigDecimal("2.50");

    @Mock
    private CurrencyConversionService conversionService;

    @InjectMocks
    private AccountBalanceService accountBalanceService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setBalancePLN(new java.math.BigDecimal(TestConstants.BALANCE_PLN));
        account.setBalanceUSD(new java.math.BigDecimal(TestConstants.BALANCE_USD));
    }

    @Test
    void calculateNewBalances_shouldReturnCorrectBalances() {
        // given
        when(conversionService.exchange(new BigDecimal(AMOUNT_10), FROM_CURRENCY, TO_CURRENCY))
            .thenReturn(EXPECTED_CONVERTED_AMOUNT);

        // when
        var result = accountBalanceService.calculateNewBalances(
            account, FROM_CURRENCY, TO_CURRENCY, new BigDecimal(AMOUNT_10)
        );

        // then
        assertEquals(EXPECTED_FROM_BALANCE, result.fromBalance());
        assertEquals(EXPECTED_TO_BALANCE, result.toBalance());
    }

    @Test
    void getBalance_shouldThrowForUnsupportedCurrency() {
        // given / when / then
        assertThrows(UnsupportedCurrencyException.class, () ->
            accountBalanceService.calculateNewBalances(account, UNSUPPORTED_CURRENCY, TO_CURRENCY, new BigDecimal(AMOUNT_10))
        );
    }

    @Test
    void setAccountBalances_shouldSetBalances() {
        // given
        AccountBalances balances = new AccountBalances(FROM_CURRENCY, BALANCE_PLN, TO_CURRENCY, BALANCE_USD);

        // when
        accountBalanceService.setAccountBalances(account, balances);

        // then
        assertEquals(BALANCE_PLN, account.getBalancePLN());
        assertEquals(BALANCE_USD, account.getBalanceUSD());
    }

    @Test
    void setBalance_shouldThrowForUnsupportedCurrency() {
        // given
        AccountBalances balances = new AccountBalances(UNSUPPORTED_CURRENCY, BALANCE_PLN, TO_CURRENCY, BALANCE_USD);

        // when / then
        assertThrows(UnsupportedCurrencyException.class, () ->
            accountBalanceService.setAccountBalances(account, balances)
        );
    }
}
