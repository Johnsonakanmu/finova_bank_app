package com.finova.transaction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.finova.transaction.roles.TransactionStatuses;
import com.finova.transaction.roles.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String counterpartyAccountNumber;

    private String reference;

    private TransactionType transactionType;

    private BigDecimal amount;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private TransactionStatuses status;

    private String description;

    private LocalDateTime createdAt;
}
