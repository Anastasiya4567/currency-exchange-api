package com.nn.task.currency.exchange.api.dto;

import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;

@Data
public class AccountInfoResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private BigDecimal balancePLN;
    private BigDecimal balanceUSD;
}