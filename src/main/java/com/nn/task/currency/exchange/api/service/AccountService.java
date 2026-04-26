package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.domain.model.AccountCreationDetails;
import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
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

    public AccountDetails createAccount(AccountCreationDetails request) {
        var account = Account.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .balancePLN(request.initialBalancePLN())
                .balanceUSD(BigDecimal.ZERO)
                .build();
        account = accountRepository.save(account);
        return toDomain(account);
    }

    public AccountDetails getAccount(UUID id) {
        var account = findAccountOrThrow(id);
        return toDomain(account);
    }

    public AccountDetails updateBalances(UUID id, BigDecimal newPLN, BigDecimal newUSD) {
        var account = findAccountOrThrow(id);
        account.setBalancePLN(newPLN);
        account.setBalanceUSD(newUSD);
        account = accountRepository.save(account);
        return toDomain(account);
    }

    private Account findAccountOrThrow(UUID id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    private AccountDetails toDomain(Account account) {
        return new AccountDetails(
                account.getId(),
                account.getFirstName(),
                account.getLastName(),
                account.getBalancePLN(),
                account.getBalanceUSD()
        );
    }
}
