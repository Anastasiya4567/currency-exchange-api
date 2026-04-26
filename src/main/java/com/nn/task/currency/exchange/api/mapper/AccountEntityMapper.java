package com.nn.task.currency.exchange.api.mapper;

import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
import com.nn.task.currency.exchange.api.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountEntityMapper {
    AccountDetails toAccountDetails(Account account);
}