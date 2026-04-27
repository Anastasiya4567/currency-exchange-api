package com.nn.task.currency.exchange.api.controller;

import com.nn.task.currency.exchange.api.mapper.AccountDetailsMapper;
import com.nn.task.currency.exchange.api.openapi.model.AccountDetailsResponse;
import com.nn.task.currency.exchange.api.openapi.model.CreateAccountRequest;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import com.nn.task.currency.exchange.api.service.AccountService;
import com.nn.task.currency.exchange.api.service.CurrencyExchangeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.AccountsApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController implements AccountsApi {
    private final AccountService accountService;
    private final AccountDetailsMapper accountDetailsMapper;
    private final CurrencyExchangeService currencyExchangeService;

    @Override
    public ResponseEntity<AccountDetailsResponse> accountsPost(@Valid @RequestBody CreateAccountRequest request) {
        var account = accountService.createAccount(accountDetailsMapper.toAccountCreationDetails(request));
        return ResponseEntity.ok(accountDetailsMapper.toAccountDetailsResponse(account));
    }

    @Override
    public ResponseEntity<AccountDetailsResponse> accountsIdGet(@PathVariable UUID id) {
        var account = accountService.getAccount(id);
        return ResponseEntity.ok(accountDetailsMapper.toAccountDetailsResponse(account));
    }

    @Override
    public ResponseEntity<AccountDetailsResponse> exchangeCurrency(UUID id, @Valid @RequestBody ExchangeRequest exchangeRequest) {
        currencyExchangeService.exchangeCurrency(id, exchangeRequest);
        var updatedAccount = accountService.getAccount(id);
        return ResponseEntity.ok(accountDetailsMapper.toAccountDetailsResponse(updatedAccount));
    }
}
