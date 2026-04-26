package com.nn.task.currency.exchange.api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateAccountRequest {
    private String firstName;
    private String lastName;
    private BigDecimal initialBalancePLN;
}