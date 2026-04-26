package com.nn.task.currency.exchange.api.domain.model;

import java.math.BigDecimal;

public class ExchangeResult {
    public final boolean success;
    public final BigDecimal newPLN;
    public final BigDecimal newUSD;

    private ExchangeResult(boolean success, BigDecimal newPLN, BigDecimal newUSD) {
        this.success = success;
        this.newPLN = newPLN;
        this.newUSD = newUSD;
    }

    public static ExchangeResult success(BigDecimal newPLN, BigDecimal newUSD) {
        return new ExchangeResult(true, newPLN, newUSD);
    }

    public static ExchangeResult failure() {
        return new ExchangeResult(false, null, null);
    }
}