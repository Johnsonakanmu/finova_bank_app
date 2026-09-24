package com.finova.address.mapper;

import com.finova.address.dto.AddressResponse;
import com.finova.address.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponse mapToResponse(Address address) {

        return AddressResponse.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .build();
    }
}
