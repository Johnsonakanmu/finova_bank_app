package com.finova.account.service;

import com.finova.account.dto.AccountRequest;
import com.finova.account.dto.AccountResponse;
import com.finova.account.dto.BalanceResponse;
import com.finova.transaction.dto.TransactionResponse;

import java.util.List;

public interface AccountService {

    public AccountResponse createAccount(AccountRequest accountRequest);
    public List<AccountResponse> getAccount();

    public AccountResponse getMyAccount(String accountNumber);

    void freezeMyAccount(String accountNumber);

    List<TransactionResponse> getMyAccountTransactions(String accountNumber);

    public BalanceResponse getBalance(String accountNumber);
}
