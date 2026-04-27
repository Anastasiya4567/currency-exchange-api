package com.nn.task.currency.exchange.api.service;

import com.nn.task.currency.exchange.api.domain.model.AccountCreationDetails;
import com.nn.task.currency.exchange.api.domain.model.AccountDetails;
import com.nn.task.currency.exchange.api.entity.Account;
import com.nn.task.currency.exchange.api.exception.AccountNotFoundException;
import com.nn.task.currency.exchange.api.exception.NegativeAmountException;
import com.nn.task.currency.exchange.api.mapper.AccountEntityMapper;
import com.nn.task.currency.exchange.api.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.nn.task.currency.exchange.api.testutil.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountEntityMapper accountEntityMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_shouldReturnAccountDetails() {
        // given
        AccountCreationDetails details = new AccountCreationDetails(FIRST_NAME, LAST_NAME, new BigDecimal(BALANCE_PLN));
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .balancePLN(new BigDecimal(BALANCE_PLN))
                .balanceUSD(BigDecimal.ZERO)
                .build();
        AccountDetails accountDetails = new AccountDetails(
            account.getId(), FIRST_NAME, LAST_NAME, new BigDecimal(BALANCE_PLN), BigDecimal.ZERO
        );
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountEntityMapper.toAccountDetails(any(Account.class))).thenReturn(accountDetails);

        // when
        AccountDetails result = accountService.createAccount(details);

        // then
        assertNotNull(result);
        assertEquals(FIRST_NAME, result.firstName());
        assertEquals(LAST_NAME, result.lastName());
        assertEquals(new BigDecimal(BALANCE_PLN), result.balancePLN());
        assertEquals(BigDecimal.ZERO, result.balanceUSD());
    }

    @Test
    void getAccount_shouldReturnAccountDetails() {
        // given
        UUID id = UUID.randomUUID();
        var account = Account.builder()
                .id(id)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .balancePLN(new BigDecimal(BALANCE_PLN))
                .balanceUSD(BigDecimal.ZERO)
                .build();
        var accountDetails = new AccountDetails(id, FIRST_NAME, LAST_NAME, new BigDecimal(BALANCE_PLN), BigDecimal.ZERO);
        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        when(accountEntityMapper.toAccountDetails(account)).thenReturn(accountDetails);

        // when
        var result = accountService.getAccount(id);

        // then
        assertNotNull(result);
        assertEquals(id, result.id());
    }

    @Test
    void getAccount_shouldThrowIfNotFound() {
        // given
        UUID id = UUID.randomUUID();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        // when / then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(id));
    }

    @Test
    void createAccount_shouldThrowIfNegativeOrZero() {
        // given
        AccountCreationDetails details = new AccountCreationDetails(FIRST_NAME, LAST_NAME, BigDecimal.ZERO);

        // when / then
        assertThrows(NegativeAmountException.class, () -> accountService.createAccount(details));
    }
}