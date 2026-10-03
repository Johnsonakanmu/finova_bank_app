package com.finova.account.service;

import com.finova.account.dto.InternalAccountResponse;

import java.util.List;

public interface InternalAccountService {

    List<InternalAccountResponse> getCustomerAccounts(Long customerId);

    public InternalAccountResponse getCustomerAccount(
            Long customerId, String accountNumber
    );
}
