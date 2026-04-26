package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.client.NbpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.function.BiFunction;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;

@Service
public class CurrencyConversionService {
    private static final Logger log = LoggerFactory.getLogger(CurrencyConversionService.class);

    private final NbpClient nbpClient;

    public CurrencyConversionService(NbpClient nbpClient) {
        this.nbpClient = nbpClient;
    }

    private static final Map<String, BiFunction<BigDecimal, CurrencyConversionService, BigDecimal>> CONVERSION_MAP = Map.of(
        PLN.name() + "->" + USD.name(),
        (amount, service) -> amount.divide(service.getUsdPlnRate(), 4, RoundingMode.HALF_UP),
        USD.name() + "->" + PLN.name(),
        (amount, service) -> amount.multiply(service.getUsdPlnRate())
    );

    public BigDecimal exchange(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return amount;
        }
        String key = fromCurrency.toUpperCase() + "->" + toCurrency.toUpperCase();
        try {
            var converter = CONVERSION_MAP.get(key);
            if (converter != null) {
                return converter.apply(amount, this);
            }
        } catch (Exception e) {
            log.warn("Currency conversion failed, falling back to 1:1 rate: {} -> {}", fromCurrency, toCurrency, e);
            return amount;
        }
        log.warn("Currency conversion not supported for pair: {} -> {}. Falling back to 1:1 rate.", fromCurrency, toCurrency);
        return amount;
    }

    public BigDecimal getUsdPlnRate() {
        try {
            var response = nbpClient.getUsdPlnRate();
            if (response != null && response.getRates() != null && !response.getRates().isEmpty()) {
                double mid = response.getRates().getFirst().getMid();
                return BigDecimal.valueOf(mid);
            }
        } catch (Exception e) {
            log.warn("Failed to fetch USD/PLN rate from NBP API, falling back to 1.0", e);
        }
        return BigDecimal.ONE;
    }
}
