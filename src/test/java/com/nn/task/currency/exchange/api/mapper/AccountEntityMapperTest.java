package com.nn.task.currency.exchange.api.mapper;

import com.nn.task.currency.exchange.api.entity.Account;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountEntityMapperTest {

    private final AccountEntityMapper mapper = new AccountEntityMapperImpl();

    @Test
    void toAccountDetails_shouldMapFieldsCorrectly() {
        // given
        Account account = Account.builder()
            .id(UUID.randomUUID())
            .firstName(FIRST_NAME)
            .lastName(LAST_NAME)
            .balancePLN(new BigDecimal(BALANCE_PLN))
            .balanceUSD(new BigDecimal(BALANCE_USD))
            .build();

        // when
        var details = mapper.toAccountDetails(account);

        // then
        assertEquals(account.getId(), details.id());
        assertEquals(FIRST_NAME, details.firstName());
        assertEquals(LAST_NAME, details.lastName());
        assertEquals(new BigDecimal(BALANCE_PLN), details.balancePLN());
        assertEquals(new BigDecimal(BALANCE_USD), details.balanceUSD());
    }
}
