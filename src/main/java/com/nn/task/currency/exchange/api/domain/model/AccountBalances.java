package com.nn.task.currency.exchange.api.domain.model;

import java.math.BigDecimal;

public record AccountBalances(
    String fromCurrency,
    BigDecimal fromBalance,
    String toCurrency,
    BigDecimal toBalance)
{}