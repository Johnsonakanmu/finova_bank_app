package com.finova.user.dto;

import com.finova.account.roles.AccountStatuses;
import com.finova.account.roles.AccountType;
import com.finova.account.roles.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountSummaryResponse {

    private String accountNumber;

    private AccountType accountType;

    private BigDecimal balance;

    private Currency currency;

    private AccountStatuses status;
}
