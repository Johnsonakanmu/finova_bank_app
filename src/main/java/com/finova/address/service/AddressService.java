package com.finova.address.service;

import com.finova.address.dto.AddressRequest;
import com.finova.address.dto.AddressResponse;

public interface AddressService {

    public AddressResponse createAddress(AddressRequest request) throws IllegalStateException;

    public  AddressResponse getMyAddress();

    public  AddressResponse updateMyAddress(AddressRequest request);

    void deleteMyAddress();
}
