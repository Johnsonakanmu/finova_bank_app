package com.finova.account.service;

import com.finova.account.dto.InternalAccountResponse;
import com.finova.account.model.Account;
import com.finova.account.repository.AccountRepository;
import com.finova.common.exception.InvalidCredentialsException;
import com.finova.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InternalAccountServiceImpl implements InternalAccountService{

    private final AccountRepository accountRepository;

    @Override
    public List<InternalAccountResponse> getCustomerAccounts(Long customerId) {

        List<Account> accounts = accountRepository.findByUser_Id(customerId);

        return accounts.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public InternalAccountResponse getCustomerAccount(Long customerId, String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new
                                ResourceNotFoundException("Account", "accountNumber", accountNumber)
                        );

        // Important: make sure this account belongs
        // to the customer being requested.
        if (!account.getUser().getId().equals(customerId)) {
            throw  new InvalidCredentialsException("Account does not belong to this customer");
        }

        return mapToResponse(account);
    }

    private InternalAccountResponse mapToResponse(Account account) {

        return InternalAccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .balance(account.getBalance())
                .currency(account.getCurrency().name())
                .status(account.getStatus().name())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
