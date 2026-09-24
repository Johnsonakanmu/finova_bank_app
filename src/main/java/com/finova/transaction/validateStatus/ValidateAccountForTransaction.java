package com.finova.transaction.validateStatus;

import com.finova.account.model.Account;
import com.finova.common.exception.AccountNotActiveException;
import org.springframework.stereotype.Component;

@Component
public class ValidateAccountForTransaction {

    public void validateAccountForTransaction(Account account) {

        switch (account.getStatus()) {

            case ACTIVE -> {
                // continue
            }

            case BLOCKED ->
                    throw new AccountNotActiveException(
                            "Account is blocked. Transactions are not allowed."
                    );

            case FROZEN ->
                    throw new AccountNotActiveException(
                            "Account is frozen. Transactions are not allowed."
                    );

            case DORMANT ->
                    throw new AccountNotActiveException(
                            "Account is dormant. Please reactivate your account."
                    );
        }
    }
}
