package com.nn.task.currency.exchange.api.constants;

import java.util.Map;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;

public final class CurrencyExchangeConstants {

    public static final Map<String, String> DEFAULT_TARGET_CURRENCY = Map.of(
        PLN.name(), USD.name(),
        USD.name(), PLN.name()
    );
}