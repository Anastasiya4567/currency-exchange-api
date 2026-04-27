package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.client.NbpRateResponse;
import com.nn.task.currency.exchange.api.client.NbpRateResponse.Rate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.nn.task.currency.exchange.api.domain.model.Currency.PLN;
import static com.nn.task.currency.exchange.api.domain.model.Currency.USD;
import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConversionServiceTest {

    private static final BigDecimal EXPECTED_RESULT_PLN_TO_USD = new BigDecimal("2.0000");
    private static final BigDecimal EXPECTED_RESULT_USD_TO_PLN = new BigDecimal("8.0000");

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private CurrencyConversionService currencyConversionService;

    @Test
    void exchange_shouldReturnSameAmountForSameCurrency() {
        // given / when
        BigDecimal result = currencyConversionService.exchange(new BigDecimal(AMOUNT_100), PLN.name(), PLN.name());

        // then
        assertEquals(new BigDecimal(AMOUNT_100), result);
    }

    @Test
    void exchange_shouldConvertPlnToUsd() {
        // given
        when(exchangeRateService.getUsdPlnRate()).thenReturn(mockNbpRateResponse(RATE_4));

        // when
        BigDecimal result = currencyConversionService.exchange(new BigDecimal(AMOUNT_8), PLN.name(), USD.name());

        // then
        assertEquals(EXPECTED_RESULT_PLN_TO_USD, result);
    }

    @Test
    void exchange_shouldConvertUsdToPln() {
        // given
        when(exchangeRateService.getUsdPlnRate()).thenReturn(mockNbpRateResponse(RATE_4));

        // when
        BigDecimal result = currencyConversionService.exchange(new BigDecimal(AMOUNT_2), USD.name(), PLN.name());

        // then
        assertEquals(EXPECTED_RESULT_USD_TO_PLN, result);
    }

    @Test
    void exchange_shouldFallbackToOneToOneForUnsupportedPair() {
        // given / when
        BigDecimal result = currencyConversionService.exchange(new BigDecimal(AMOUNT_5), EUR, PLN.name());

        // then
        assertEquals(new BigDecimal(AMOUNT_5), result);
    }

    @Test
    void getUsdPlnRate_shouldReturnRateFromService() {
        // given
        when(exchangeRateService.getUsdPlnRate()).thenReturn(mockNbpRateResponse(RATE_4_2));

        // when
        BigDecimal rate = currencyConversionService.getUsdPlnRate();

        // then
        assertEquals(new BigDecimal(RATE_4_2), rate);
    }

    @Test
    void getUsdPlnRate_shouldReturnOneOnException() {
        // given
        when(exchangeRateService.getUsdPlnRate()).thenThrow(new RuntimeException("fail"));

        // when
        BigDecimal rate = currencyConversionService.getUsdPlnRate();

        // then
        assertEquals(BigDecimal.ONE, rate);
    }

    private NbpRateResponse mockNbpRateResponse(String mid) {
        NbpRateResponse response = new NbpRateResponse();
        Rate rate = new Rate();
        rate.setMid(new BigDecimal(mid));
        response.setRates(List.of(rate));
        return response;
    }
}
