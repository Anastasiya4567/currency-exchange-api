package com.nn.task.currency.exchange.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CurrencyExchangeAPIApplication {

	static void main(String[] args) {
		SpringApplication.run(CurrencyExchangeAPIApplication.class, args);
	}

}
