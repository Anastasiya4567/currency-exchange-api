package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.dto.AccountInfoResponse;
import com.nn.task.currency.exchange.api.dto.CreateAccountRequest;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountInfoResponse createAccount(CreateAccountRequest request) {
        var account = Account.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .balancePLN(request.initialBalancePLN())
                .balanceUSD(BigDecimal.ZERO)
                .build();
        account = accountRepository.save(account);
        return toResponse(account);
    }

    public AccountInfoResponse getAccount(UUID id) {
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return toResponse(account);
    }

    private AccountInfoResponse toResponse(Account account) {
        return new AccountInfoResponse(
                account.getId(),
                account.getFirstName(),
                account.getLastName(),
                account.getBalancePLN(),
                account.getBalanceUSD()
        );
    }
}
