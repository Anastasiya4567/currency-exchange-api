package com.nn.task.currency.exchange.api.dto;

import java.util.UUID;
import java.math.BigDecimal;

public record AccountInfoResponse(
    UUID id,
    String firstName,
    String lastName,
    BigDecimal balancePLN,
    BigDecimal balanceUSD
) {}
