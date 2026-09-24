package com.finova.address.service;

import com.finova.address.dto.AddressRequest;
import com.finova.address.dto.AddressResponse;
import com.finova.address.mapper.AddressMapper;
import com.finova.address.model.Address;
import com.finova.autheticatedUser.GetAuthenticatedUser;
import com.finova.common.exception.ResourceNotFoundException;
import com.finova.user.model.User;
import com.finova.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService{

    private UserRepository userRepository;
    private AddressMapper addressMapper;
    private GetAuthenticatedUser getAuthenticatedUser;

    @Override
    public AddressResponse createAddress(AddressRequest request) {


        User user = getAuthenticatedUser.getAuthenticatedUser();

        if (user.getAddress() != null) {
            throw  new IllegalStateException(
                    "User already has an address"
            );
        }

        Address address = Address.builder()
                .street(request.getStreet())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .build();

        user.setAddress(address);

        User savedUser = userRepository.saveAndFlush(user);

        Address savedAddress = savedUser.getAddress();

        return AddressResponse.builder()
                .id(savedAddress.getId())
                .street(savedAddress.getStreet())
                .state(address.getState())
                .country(savedAddress.getCountry())
                .city(savedAddress.getCity())
                .postalCode(savedAddress.getPostalCode())
                .build();

    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getMyAddress() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "email",
                                email
                        )
                );

        if (user.getAddress() == null) {
            throw new ResourceNotFoundException(
                    "Address", "user", email
            );
        }
        return addressMapper.mapToResponse(user.getAddress());
    }

    @Override
    public AddressResponse updateMyAddress(AddressRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "email",
                                email
                        )
                );

        Address address = user.getAddress();

        if (address == null) {
            throw new ResourceNotFoundException(
                    "Address", "user", email
            );
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());
        address.setPostalCode(request.getPostalCode());

        User savedUser = userRepository.saveAndFlush(user);

        return addressMapper.mapToResponse(savedUser.getAddress());
    }

    @Override
    public void deleteMyAddress() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "email",
                                email
                        )
                );

        if (user.getAddress() == null) {
            throw new ResourceNotFoundException(
                    "Address",
                    "user",
                    email
            );
        }

        user.setAddress(null);

        userRepository.saveAndFlush(user);

    }

}
