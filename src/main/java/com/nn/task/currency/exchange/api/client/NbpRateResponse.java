package com.nn.task.currency.exchange.api.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class NbpRateResponse {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;

    @Setter
    @Getter
    public static class Rate {
        private String no;
        private String effectiveDate;
        private BigDecimal mid;

    }
}
