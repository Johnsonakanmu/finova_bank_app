package com.finova.transaction.service;

import com.finova.transaction.dto.*;

import java.time.LocalDate;

public interface TransactionService {

    public TransactionResponse deposit(DepositRequest request);

    public TransactionResponse withdraw(WithdrawRequest request);

    public TransactionResponse transfer(TransferRequest request);

    public StatementResponse getStatement(
            String accountNumber,
            LocalDate from,
            LocalDate to
    );
}
