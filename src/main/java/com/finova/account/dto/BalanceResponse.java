package com.finova.account.dto;

import com.finova.account.roles.AccountStatuses;
import com.finova.account.roles.Currency;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BalanceResponse {

    private String accountNumber;
    private BigDecimal balance;
    private Currency currency;
    private AccountStatuses status;
}
