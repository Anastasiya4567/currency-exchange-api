package com.nn.task.currency.exchange.api.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountDetails(
    UUID id,
    String firstName,
    String lastName,
    BigDecimal balancePLN,
    BigDecimal balanceUSD
) {
}
