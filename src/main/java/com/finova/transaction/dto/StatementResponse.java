package com.finova.transaction.dto;

import com.finova.account.roles.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementResponse {

    private String accountNumber;
    private Currency currency;

    private LocalDate from;
    private LocalDate to;

    private BigDecimal openingBalance;
    private BigDecimal closingBalance;

    private List<TransactionResponse> transactions;
}
