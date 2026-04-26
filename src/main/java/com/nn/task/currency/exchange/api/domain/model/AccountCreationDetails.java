package com.nn.task.currency.exchange.api.domain.model;

import java.math.BigDecimal;

public record AccountCreationDetails(
    String firstName,
    String lastName,
    BigDecimal initialBalancePLN
) {
}