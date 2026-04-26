package com.nn.task.currency.exchange.api.dto;

import java.math.BigDecimal;

public record ExchangeRequest(
    String fromCurrency, // "PLN" or "USD"
    BigDecimal amount
) {}
