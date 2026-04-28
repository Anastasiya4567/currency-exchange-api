package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.exception.InsufficientFundsException;
import com.nn.task.currency.exchange.api.exception.NegativeAmountException;
import com.nn.task.currency.exchange.api.exception.UnsupportedCurrencyException;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExchangeValidatorTest {
    private static final BigDecimal INSUFFICIENT_AMOUNT = new BigDecimal("1000.00");

    private ExchangeValidator validator;
    private Account account;

    @BeforeEach
    void setUp() {
        validator = new ExchangeValidator();
        account = Account.builder()
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .balancePLN(new BigDecimal(BALANCE_PLN))
            .balanceUSD(new BigDecimal(BALANCE_USD))
            .build();
    }

    @Test
    void validateAll_shouldThrowForNegativeAmount() {
        // given
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setFromCurrency(FROM_CURRENCY);
        exchangeRequest.setAmount(NEGATIVE_AMOUNT);

        // when / then
        assertThrows(NegativeAmountException.class, () -> validator.validateAll(account, exchangeRequest));
    }

    @Test
    void validateAll_shouldThrowForUnsupportedCurrency() {
        // given
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setFromCurrency(UNSUPPORTED_CURRENCY);
        exchangeRequest.setAmount(AMOUNT_10);

        // when / then
        assertThrows(UnsupportedCurrencyException.class, () -> validator.validateAll(account, exchangeRequest));
    }

    @Test
    void validateAll_shouldThrowForInsufficientBalance() {
        // given
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setFromCurrency(FROM_CURRENCY);
        exchangeRequest.setAmount(INSUFFICIENT_AMOUNT.toPlainString());

        // when / then
        assertThrows(InsufficientFundsException.class, () -> validator.validateAll(account, exchangeRequest));
    }

    @Test
    void validateAll_shouldPassForValidRequest() {
        // given
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setFromCurrency(FROM_CURRENCY);
        exchangeRequest.setAmount(AMOUNT_10);

        // when / then
        assertDoesNotThrow(() -> validator.validateAll(account, exchangeRequest));
    }

    @Test
    void validateAll_shouldThrowForZeroAmount() {
        // given
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setFromCurrency(FROM_CURRENCY);
        exchangeRequest.setAmount(BigDecimal.ZERO.toPlainString());

        // when / then
        assertThrows(NegativeAmountException.class, () -> validator.validateAll(account, exchangeRequest));
    }

    @Test
    void validateAll_shouldThrowForInsufficientUSDBalance() {
        // given
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setFromCurrency(TO_CURRENCY);
        exchangeRequest.setAmount(INSUFFICIENT_AMOUNT.toPlainString());

        // when / then
        assertThrows(InsufficientFundsException.class, () -> validator.validateAll(account, exchangeRequest));
    }

    @Test
    void validateAll_shouldPassForValidUSDRequest() {
        // given
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setFromCurrency(TO_CURRENCY);
        exchangeRequest.setAmount(AMOUNT_10);

        // when / then
        assertDoesNotThrow(() -> validator.validateAll(account, exchangeRequest));
    }

    @Test
    void validateAll_shouldBeCaseInsensitiveForCurrency() {
        // given
        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setFromCurrency(FROM_CURRENCY.toLowerCase());
        exchangeRequest.setAmount(AMOUNT_10);

        // when / then
        assertDoesNotThrow(() -> validator.validateAll(account, exchangeRequest));
    }
}