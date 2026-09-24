package com.finova.transaction.repository;

import com.finova.account.model.Account;
import com.finova.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);

    Optional<Transaction> findByReference(String reference);

    List<Transaction> findByAccountAndCreatedAtBetween(
            Account account,
            LocalDateTime from,
            LocalDateTime to
    );
}
