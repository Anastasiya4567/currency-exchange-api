package com.nn.task.currency.exchange.api.dto;

import java.math.BigDecimal;

public record CreateAccountRequest(
    String firstName,
    String lastName,
    BigDecimal initialBalancePLN
) {}
