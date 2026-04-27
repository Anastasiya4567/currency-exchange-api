package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.client.NbpClient;
import com.nn.task.currency.exchange.api.client.NbpRateResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {
    private final NbpClient nbpClient;

    public ExchangeRateService(NbpClient nbpClient) {
        this.nbpClient = nbpClient;
    }

    @Cacheable("usdPlnRate")
    public NbpRateResponse getUsdPlnRate() {
        return nbpClient.getUsdPlnRate();
    }
}