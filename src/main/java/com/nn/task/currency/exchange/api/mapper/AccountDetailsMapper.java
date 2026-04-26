package com.nn.task.currency.exchange.api.mapper;

import com.nn.task.currency.exchange.api.domain.model.AccountCreationDetails;
import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
import com.nn.task.currency.exchange.api.openapi.model.AccountDetailsResponse;
import com.nn.task.currency.exchange.api.openapi.model.CreateAccountRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountDetailsMapper {
    AccountCreationDetails toAccountCreationDetails(CreateAccountRequest request);

    AccountDetailsResponse toAccountDetailsResponse(AccountDetails details);
}
