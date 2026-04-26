package com.nn.task.currency.exchange.api.repository;

import com.nn.task.currency.exchange.api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}