package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.domain.model.AccountCreationDetails;
import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.exception.AccountNotFoundException;
import com.nn.task.currency.exchange.api.exception.NegativeAmountException;
import com.nn.task.currency.exchange.api.mapper.AccountEntityMapper;
import com.nn.task.currency.exchange.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountEntityMapper accountEntityMapper;

    public AccountDetails createAccount(AccountCreationDetails request) {
        if (request.initialBalancePLN() == null || request.initialBalancePLN().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeAmountException();
        }
        var account = Account.builder()
            .firstName(request.firstName())
            .lastName(request.lastName())
            .balancePLN(request.initialBalancePLN())
            .balanceUSD(BigDecimal.ZERO)
            .build();
        account = saveAccount(account);
        return accountEntityMapper.toAccountDetails(account);
    }

    public AccountDetails getAccount(UUID id) {
        var account = findAccount(id);
        return accountEntityMapper.toAccountDetails(account);
    }

    public Account findAccount(UUID id) {
        return accountRepository.findById(id)
            .orElseThrow(AccountNotFoundException::new);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }
}
