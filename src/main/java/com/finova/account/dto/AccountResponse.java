package com.finova.account.dto;

import com.finova.account.roles.AccountStatuses;
import com.finova.account.roles.AccountType;
import com.finova.account.roles.Currency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AccountResponse {

    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private Currency currency;
    private AccountStatuses status;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
