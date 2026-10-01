package com.finova.transaction.service;

import com.finova.account.model.Account;
import com.finova.account.repository.AccountRepository;
import com.finova.account.roles.AccountStatuses;
import com.finova.autheticatedUser.GetAuthenticatedUser;
import com.finova.beneficiary.model.Beneficiary;
import com.finova.beneficiary.repository.BeneficiaryRepository;
import com.finova.common.exception.InsufficientBalanceException;
import com.finova.common.exception.ResourceNotFoundException;
import com.finova.transaction.dto.*;
import com.finova.transaction.mapper.TransactionMapper;
import com.finova.transaction.model.Transaction;
import com.finova.transaction.referenceGenerator.TransactionReferenceGenerator;
import com.finova.transaction.repository.TransactionRepository;
import com.finova.transaction.roles.TransactionStatuses;
import com.finova.transaction.roles.TransactionType;
import com.finova.transaction.validateStatus.ValidateAccountForTransaction;
import com.finova.user.model.User;
import com.finova.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final GetAuthenticatedUser getAuthenticatedUser;
    private final TransactionMapper transactionMapper;
    private final BeneficiaryRepository beneficiaryRepository;
    private final ValidateAccountForTransaction validateAccountForTransaction;

    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {

        // Get authenticated user
        User user = getAuthenticatedUser.getAuthenticatedUser();

        // Find user's account
        Account account = accountRepository
                .findByIdAndUser(request.getAccountId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account",
                                "id",
                                request.getAccountId().toString()
                        )
                );

        // Validate transaction type
        if (request.getTransactionType() != TransactionType.DEPOSIT) {
            throw new IllegalArgumentException(
                    "Transaction type must be DEPOSIT"
            );
        }

        // Validate account status
        validateAccountForTransaction.validateAccountForTransaction(account);

        // Calculate balances
        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(request.getAmount());

        // Update account balance
        account.setBalance(balanceAfter);
        accountRepository.save(account);

        // Create transaction
        Transaction transaction = Transaction.builder()
                .reference(TransactionReferenceGenerator.generate())
                .account(account)
                .transactionType(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .status(TransactionStatuses.SUCCESS)
                .description(request.getDescription())
                .build();

        // Save transaction
        transaction = transactionRepository.save(transaction);

        // Return response
        return transactionMapper.mapToResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {

        // Get authenticated user
        User user = getAuthenticatedUser.getAuthenticatedUser();

        // Find account belonging to authenticated user
        Account account = accountRepository
                .findByIdAndUser(request.getAccountId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account",
                                "id",
                                request.getAccountId().toString()
                        )
                );

        // Validate transaction type
        if (request.getTransactionType() != TransactionType.WITHDRAWAL) {
            throw new IllegalArgumentException(
                    "Transaction type must be WITHDRAWAL"
            );
        }

        // Validate account status
        if (account.getStatus() != AccountStatuses.ACTIVE) {
            throw new IllegalStateException(
                    "Transactions are not allowed on a " +
                            account.getStatus().name().toLowerCase() +
                            " account"
            );
        }

        // Validate transaction type
        if (request.getTransactionType() != TransactionType.WITHDRAWAL) {
            throw new IllegalArgumentException(
                    "Transaction type must be WITHDRAWAL"
            );
        }

        // Validate account status
        validateAccountForTransaction.validateAccountForTransaction(account);

        // Get current balance
        BigDecimal balanceBefore = account.getBalance();

        // Check sufficient balance
        if (balanceBefore.compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance for this withdrawal"
            );
        }

        // Calculate new balance
        BigDecimal balanceAfter =
                balanceBefore.subtract(request.getAmount());

        // Update account balance
        account.setBalance(balanceAfter);

        accountRepository.save(account);

        // Create transaction
        Transaction transaction = Transaction.builder()
                .reference(TransactionReferenceGenerator.generate())
                .account(account)
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .status(TransactionStatuses.SUCCESS)
                .description(request.getDescription())
                .build();

        // Save transaction
        transaction = transactionRepository.save(transaction);

        // Return response
        return transactionMapper.mapToResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        User user = getAuthenticatedUser.getAuthenticatedUser();

        // 1. Validate transaction type
        if (request.getTransactionType() != TransactionType.TRANSFER) {
            throw new IllegalArgumentException(
                    "Transaction type must be TRANSFER"
            );
        }

        // 2. Find sender's account
        Account sourceAccount = accountRepository
                .findByIdAndUser(request.getAccountId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account",
                                "id",
                                request.getAccountId().toString()
                        )
                );

        // Validate sender account
        validateAccountForTransaction
                .validateAccountForTransaction(sourceAccount);


        // 3. Determine destination account number
        String destinationAccountNumber;

        if (request.getBeneficiaryId() != null) {

            // Transfer using saved beneficiary
            Beneficiary beneficiary = beneficiaryRepository
                    .findByIdAndUser(
                            request.getBeneficiaryId(),
                            user
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Beneficiary",
                                    "id",
                                    request.getBeneficiaryId().toString()
                            )
                    );

            destinationAccountNumber = beneficiary.getAccountNumber();

        } else if (request.getDestinationAccountNumber() != null
                && !request.getDestinationAccountNumber().isBlank()) {

            // Direct transfer using account number
            destinationAccountNumber =
                    request.getDestinationAccountNumber();

        } else {

            throw new IllegalArgumentException(
                    "Either beneficiaryId or destinationAccountNumber is required"
            );
        }


        // 4. Find receiver's account by account number
        Account counterpartyAccount = accountRepository
                .findByAccountNumber(destinationAccountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account",
                                "account number",
                                destinationAccountNumber
                        )
                );

        // Validate receiver account
        validateAccountForTransaction
                .validateAccountForTransaction(counterpartyAccount);


        // 5. Don't allow transfer to yourself
        if (sourceAccount.getId()
                .equals(counterpartyAccount.getId())) {

            throw new IllegalArgumentException(
                    "You cannot transfer money to your own account"
            );
        }

        //
        if (!sourceAccount.getCurrency().equals(counterpartyAccount.getCurrency())) {
            throw new IllegalArgumentException("Cannot transfer between accounts with different currencies");
        }


        // 6. Check balance
        BigDecimal sourceBalanceBefore =
                sourceAccount.getBalance();

        if (sourceBalanceBefore.compareTo(request.getAmount()) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance for this transfer"
            );
        }


        // 7. Calculate balances
        BigDecimal sourceBalanceAfter =
                sourceBalanceBefore.subtract(request.getAmount());

        BigDecimal destinationBalanceBefore =
                counterpartyAccount.getBalance();

        BigDecimal destinationBalanceAfter =
                destinationBalanceBefore.add(request.getAmount());


        // 8. Update balances
        sourceAccount.setBalance(sourceBalanceAfter);
        counterpartyAccount.setBalance(destinationBalanceAfter);

        accountRepository.save(sourceAccount);
        accountRepository.save(counterpartyAccount);


        // 9. Create sender transaction
        Transaction sourceTransaction = Transaction.builder()
                .reference(TransactionReferenceGenerator.generate())
                .account(sourceAccount)
                .counterpartyAccount(counterpartyAccount)
                .transactionType(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .balanceBefore(sourceBalanceBefore)
                .balanceAfter(sourceBalanceAfter)
                .status(TransactionStatuses.SUCCESS)
                .description(request.getDescription())
                .build();


        // 10. Create receiver transaction
        Transaction destinationTransaction = Transaction.builder()
                .reference(TransactionReferenceGenerator.generate())
                .account(counterpartyAccount)
                .counterpartyAccount(sourceAccount)
                .transactionType(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .balanceBefore(destinationBalanceBefore)
                .balanceAfter(destinationBalanceAfter)
                .status(TransactionStatuses.SUCCESS)
                .description(
                        "Transfer received from account "
                                + sourceAccount.getAccountNumber()
                )
                .build();


        // 11. Save transactions
        transactionRepository.save(sourceTransaction);
        transactionRepository.save(destinationTransaction);


        // 12. Return sender transaction
        return transactionMapper.mapToResponse(sourceTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public StatementResponse getStatement(String accountNumber, LocalDate from, LocalDate to) {

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

        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "From date cannot be after to date"
            );
        }

        LocalDateTime startDate =
                from.atStartOfDay();

        LocalDateTime endDate =
                to.plusDays(1).atStartOfDay().minusNanos(1);

        List<Transaction> transactions =
                transactionRepository
                        .findByAccountAndCreatedAtBetween(
                                account,
                                startDate,
                                endDate
                        );

        List<TransactionResponse> transactionResponses =
                transactions.stream()
                        .map(transactionMapper::mapToResponse)
                        .toList();

        return StatementResponse.builder()
                .accountNumber(account.getAccountNumber())
                .currency(account.getCurrency())
                .from(from)
                .to(to)
                .closingBalance(account.getBalance())
                .transactions(transactionResponses)
                .build();

    }

}
