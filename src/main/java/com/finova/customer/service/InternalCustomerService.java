package com.finova.customer.service;

import com.finova.customer.dto.InternalCustomerResponse;

import java.util.List;

public interface InternalCustomerService {

    List<InternalCustomerResponse> searchCustomers(
            String firstName,
            String lastName,
            String email,
            String phoneNumber
    );

    InternalCustomerResponse getCustomerById(Long id);
}