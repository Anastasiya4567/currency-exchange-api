package com.nn.task.currency.exchange.api.controller;

import com.nn.task.currency.exchange.api.domain.model.AccountCreationDetails;
import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
import com.nn.task.currency.exchange.api.mapper.AccountDetailsMapper;
import com.nn.task.currency.exchange.api.openapi.model.AccountDetailsResponse;
import com.nn.task.currency.exchange.api.openapi.model.CreateAccountRequest;
import com.nn.task.currency.exchange.api.openapi.model.ExchangeRequest;
import com.nn.task.currency.exchange.api.service.AccountService;
import com.nn.task.currency.exchange.api.service.CurrencyExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private AccountDetailsMapper accountDetailsMapper;

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void accountsPost_shouldReturnAccountDetailsResponse() {
        // given
        CreateAccountRequest request = new CreateAccountRequest();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setInitialBalancePLN(BALANCE_PLN);
        AccountCreationDetails details = new AccountCreationDetails(FIRST_NAME, LAST_NAME, new BigDecimal(BALANCE_PLN));
        AccountDetails accountDetails = new AccountDetails(
            UUID.randomUUID(), FIRST_NAME, LAST_NAME, new BigDecimal(BALANCE_PLN), BigDecimal.ZERO
        );
        AccountDetailsResponse response = new AccountDetailsResponse();
        when(accountDetailsMapper.toAccountCreationDetails(request)).thenReturn(details);
        when(accountService.createAccount(details)).thenReturn(accountDetails);
        when(accountDetailsMapper.toAccountDetailsResponse(accountDetails)).thenReturn(response);

        // when
        var result = accountController.accountsPost(request);

        // then
        assertNotNull(result);
        assertEquals(response, result.getBody());
    }

    @Test
    void accountsIdGet_shouldReturnAccountDetailsResponse() {
        // given
        UUID id = UUID.randomUUID();
        AccountDetails accountDetails = new AccountDetails(id, FIRST_NAME, LAST_NAME, new BigDecimal(BALANCE_PLN), BigDecimal.ZERO);
        AccountDetailsResponse response = new AccountDetailsResponse();
        when(accountService.getAccount(id)).thenReturn(accountDetails);
        when(accountDetailsMapper.toAccountDetailsResponse(accountDetails)).thenReturn(response);

        // when
        var result = accountController.accountsIdGet(id);

        // then
        assertNotNull(result);
        assertEquals(response, result.getBody());
    }

    @Test
    void exchangeCurrency_shouldReturnUpdatedAccountDetailsResponse() {
        // given
        UUID id = UUID.randomUUID();
        var exchangeRequest = mock(ExchangeRequest.class);
        var updatedAccount = mock(AccountDetails.class);
        var response = mock(AccountDetailsResponse.class);
        doNothing().when(currencyExchangeService).exchangeCurrency(id, exchangeRequest);
        when(accountService.getAccount(id)).thenReturn(updatedAccount);
        when(accountDetailsMapper.toAccountDetailsResponse(updatedAccount)).thenReturn(response);

        // when
        var result = accountController.exchangeCurrency(id, exchangeRequest);

        // then
        assertNotNull(result);
        assertEquals(response, result.getBody());
        verify(currencyExchangeService).exchangeCurrency(id, exchangeRequest);
        verify(accountService).getAccount(id);
        verify(accountDetailsMapper).toAccountDetailsResponse(updatedAccount);
    }
}
