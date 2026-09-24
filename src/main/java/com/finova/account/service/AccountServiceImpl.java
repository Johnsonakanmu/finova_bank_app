package com.finova.account.service;

import com.finova.account.dto.AccountRequest;
import com.finova.account.dto.AccountResponse;
import com.finova.account.dto.BalanceResponse;
import com.finova.account.mapper.AccountMapper;
import com.finova.account.model.Account;
import com.finova.account.repository.AccountRepository;
import com.finova.account.roles.AccountStatuses;
import com.finova.account.roles.Currency;
import com.finova.autheticatedUser.GetAuthenticatedUser;
import com.finova.common.exception.AccountAlreadyExistsException;
import com.finova.common.exception.ResourceNotFoundException;
import com.finova.transaction.dto.TransactionResponse;
import com.finova.transaction.model.Transaction;
import com.finova.transaction.repository.TransactionRepository;
import com.finova.user.model.User;
import com.finova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService{

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final GetAuthenticatedUser getAuthenticatedUser;
    private final AccountMapper accountMapper;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {

        // Get authenticated user's email from JWT
        User user = getAuthenticatedUser.getAuthenticatedUser();

        // Check if user already has this account type
        // in this currency
        boolean accountExists =
                accountRepository.existsByUserAndAccountTypeAndCurrency(
                        user,
                        accountRequest.getAccountType(),
                        accountRequest.getCurrency()
                );

        if (accountExists) {
            throw new AccountAlreadyExistsException(
                    "You already have a " +
                            accountRequest.getAccountType() +
                            " account in " +
                            accountRequest.getCurrency()
            );
        }

        // General Unique Account Number
        String accountNumber = generateAccountNumber();

        // Create Account
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(accountRequest.getAccountType())
                .balance(BigDecimal.ZERO.setScale(2))
                .currency(accountRequest.getCurrency())
                .status(AccountStatuses.ACTIVE)
                .user(user)
                .build();

        //Save Account
        Account savedAccount = accountRepository.save(account);

        // convert entity to response
        return accountMapper.mapToResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccount() {

        User user = getAuthenticatedUser.getAuthenticatedUser();

        List<Account> accounts = accountRepository.findByUser(user);

        return accounts.stream()
                .map(accountMapper::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getMyAccount(String accountNumber) {

        User user = getAuthenticatedUser.getAuthenticatedUser();

        Account account = accountRepository
                .findByAccountNumberAndUser(accountNumber, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account",
                                "account number",
                                accountNumber
                        )
                );

        return accountMapper.mapToResponse(account);
    }

    @Override
    @Transactional
    public void freezeMyAccount(String accountNumber) {



        User user = getAuthenticatedUser.getAuthenticatedUser();

        Account account = accountRepository
                .findByAccountNumberAndUser(accountNumber, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account",
                                "account number",
                                accountNumber
                        )
                );

        if (account.getStatus() == AccountStatuses.BLOCKED) {
            throw new IllegalStateException(
                    "Blocked account cannot be frozen"
            );
        }

        if (account.getStatus() == AccountStatuses.FROZEN) {
            throw new IllegalStateException(
                    "Account is already frozen"
            );
        }

        account.setStatus(AccountStatuses.FROZEN);

        accountRepository.save(account);

    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getMyAccountTransactions(String accountNumber) {



        User user = getAuthenticatedUser.getAuthenticatedUser();

        Account account = accountRepository
                .findByAccountNumberAndUser(accountNumber, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account",
                                "account number",
                                accountNumber
                        )
                );

        List<Transaction> transactions =
                transactionRepository.findByAccount(account);

        return transactions.stream()
                .map(this::mapTransactionToResponse)
                .toList();

    }

    @Override
    public BalanceResponse getBalance(String accountNumber) {


        User user = getAuthenticatedUser.getAuthenticatedUser();

        Account account = accountRepository
                .findByAccountNumberAndUser(accountNumber, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account",
                                "account number",
                                accountNumber
                        )
                );

        return BalanceResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .build();
    }



    private String generateAccountNumber() {

        String accountNumber;

        do {
            accountNumber = String.valueOf(
                    ThreadLocalRandom.current()
                            .nextLong(1_000_000_000L, 10_000_000_000L)
            );

        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }





    private TransactionResponse mapTransactionToResponse(
            Transaction transaction
    ) {

        return TransactionResponse.builder()
                .id(transaction.getId())
                .reference(transaction.getReference())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .balanceBefore(transaction.getBalanceBefore())
                .balanceAfter(transaction.getBalanceAfter())
                .status(transaction.getStatus())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
