package com.finova.account.repository;

import com.finova.account.model.Account;
import com.finova.account.roles.AccountType;
import com.finova.account.roles.Currency;
import com.finova.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByUserAndAccountTypeAndCurrency(
            User user,
            AccountType accountType,
            Currency currency
    );

    List<Account> findByUser(User user);

    Optional<Account> findByAccountNumberAndUser(
            String accountNumber,
            User user
    );

    Optional<Account> findByIdAndUser(Long id, User user);

    List<Account> findByUser_Id(Long userId);
}
