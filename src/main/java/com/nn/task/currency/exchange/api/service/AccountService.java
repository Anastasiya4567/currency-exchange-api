package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.dto.AccountInfoResponse;
import com.nn.task.currency.exchange.api.dto.CreateAccountRequest;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountInfoResponse createAccount(CreateAccountRequest request) {
        Account account = Account.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .balancePLN(request.getInitialBalancePLN())
                .balanceUSD(BigDecimal.ZERO)
                .build();
        account = accountRepository.save(account);
        return toResponse(account);
    }

    private AccountInfoResponse toResponse(Account account) {
        AccountInfoResponse response = new AccountInfoResponse();
        response.setId(account.getId());
        response.setFirstName(account.getFirstName());
        response.setLastName(account.getLastName());
        response.setBalancePLN(account.getBalancePLN());
        response.setBalanceUSD(account.getBalanceUSD());
        return response;
    }
}

