package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.client.NbpClient;
import com.nn.task.currency.exchange.api.client.NbpRateResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private NbpClient nbpClient;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Test
    void getUsdPlnRate_shouldReturnResponse() {
        // given
        NbpRateResponse response = new NbpRateResponse();
        when(nbpClient.getUsdPlnRate()).thenReturn(response);

        // when
        var result = exchangeRateService.getUsdPlnRate();

        // then
        assertNotNull(result);
        assertEquals(response, result);
        verify(nbpClient, times(1)).getUsdPlnRate();
    }
}