package com.finova.transaction.service;

import com.finova.transaction.dto.StatementResponse;
import com.finova.transaction.dto.TransactionRequest;
import com.finova.transaction.dto.TransactionResponse;

import java.time.LocalDate;

public interface TransactionService {

    public TransactionResponse deposit(TransactionRequest request);

    public TransactionResponse withdraw(TransactionRequest request);

    public TransactionResponse transfer(TransactionRequest request);

    public StatementResponse getStatement(
            String accountNumber,
            LocalDate from,
            LocalDate to
    );
}
