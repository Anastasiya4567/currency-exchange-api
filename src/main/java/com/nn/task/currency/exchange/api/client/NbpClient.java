package com.nn.task.currency.exchange.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "nbpClient", url = "http://api.nbp.pl/api")
public interface NbpClient {

    @GetMapping("/exchangerates/rates/A/USD/?format=json")
    NbpRateResponse getUsdPlnRate();
}

