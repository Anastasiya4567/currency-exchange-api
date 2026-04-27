package com.nn.task.currency.exchange.api.mapper;

import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
import com.nn.task.currency.exchange.api.openapi.model.CreateAccountRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountDetailsMapperTest {

    private final AccountDetailsMapper mapper = new AccountDetailsMapperImpl();

    @Test
    void toAccountCreationDetails_shouldMapFieldsCorrectly() {
        // given
        CreateAccountRequest request = new CreateAccountRequest();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setInitialBalancePLN(BALANCE_PLN);

        // when
        var details = mapper.toAccountCreationDetails(request);

        // then
        assertEquals(FIRST_NAME, details.firstName());
        assertEquals(LAST_NAME, details.lastName());
        assertEquals(new BigDecimal(BALANCE_PLN), details.initialBalancePLN());
    }

    @Test
    void toAccountDetailsResponse_shouldMapFieldsCorrectly() {
        // given
        AccountDetails details = new AccountDetails(
            UUID.randomUUID(), FIRST_NAME, LAST_NAME, new BigDecimal(BALANCE_PLN), new BigDecimal(BALANCE_USD)
        );

        // when
        var response = mapper.toAccountDetailsResponse(details);

        // then
        assertEquals(FIRST_NAME, response.getFirstName());
        assertEquals(LAST_NAME, response.getLastName());
        assertEquals(BALANCE_PLN, response.getBalancePLN());
        assertEquals(BALANCE_USD, response.getBalanceUSD());
    }
}
