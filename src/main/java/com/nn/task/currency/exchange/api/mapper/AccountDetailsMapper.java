package com.nn.task.currency.exchange.api.mapper;

import com.nn.task.currency.exchange.api.domain.model.AccountCreationDetails;
import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
import com.nn.task.currency.exchange.api.openapi.model.AccountDetailsResponse;
import com.nn.task.currency.exchange.api.openapi.model.CreateAccountRequest;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AccountDetailsMapper {
    AccountCreationDetails toAccountCreationDetails(CreateAccountRequest request);

    AccountDetailsResponse toAccountDetailsResponse(AccountDetails details);

    // TODO
    default AccountDetails toAccountDetails(com.nn.task.currency.exchange.api.entity.Account account) {
        if (account == null) return null;
        return new AccountDetails(
            account.getId(),
            account.getFirstName(),
            account.getLastName(),
            account.getBalancePLN(),
            account.getBalanceUSD()
        );
    }

    default BigDecimal map(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    default Double map(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }
}
