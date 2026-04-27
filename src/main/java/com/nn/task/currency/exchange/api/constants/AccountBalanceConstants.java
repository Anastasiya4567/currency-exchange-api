package com.nn.task.currency.exchange.api.constants;

import com.nn.task.currency.exchange.api.entity.Account;
import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;

public final class AccountBalanceConstants {
    private AccountBalanceConstants() {}

    public static final Map<String, Function<Account, BigDecimal>> BALANCE_GETTERS = Map.of(
        PLN.name(), Account::getBalancePLN,
        USD.name(), Account::getBalanceUSD
    );

    public static final Map<String, BiConsumer<Account, BigDecimal>> BALANCE_SETTERS = Map.of(
        PLN.name(), Account::setBalancePLN,
        USD.name(), Account::setBalanceUSD
    );
}

