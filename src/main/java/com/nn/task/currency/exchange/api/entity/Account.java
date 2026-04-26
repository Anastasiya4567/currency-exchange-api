package com.nn.task.currency.exchange.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balancePLN;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balanceUSD;
}