package com.finova.user.service;

import com.finova.address.dto.AddressResponse;
import com.finova.address.model.Address;
import com.finova.common.exception.ResourceNotFoundException;
import com.finova.user.dto.*;
import com.finova.user.model.User;
import com.finova.user.repository.UserRepository;
import com.finova.user.roles.UserStatuses;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService{

    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers(UserStatuses statuses) {
        List<User> users;

        if (statuses != null) {
            users = userRepository.findByStatus(statuses);
        } else {
            users = userRepository.findAll();
        }

        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("user", "id", id)
                        );
        return mapToResponse(user);
    }

    @Override
    public UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id)
                        );
        user.setStatus(request.getStatuses());

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User", "id", id)
                        );

        user.setStatus(UserStatuses.BLOCKED);
        userRepository.save(user);

    }

    @Override
    public List<AccountLookupResponse> findCustomers(AccountLookupRequest request) {

        List<User> users = userRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
                request.getFirstName(),
                request.getLastName()
        );

        if (users.isEmpty()) {
            throw new ResourceNotFoundException(
                    "User",
                    "name",
                    request.getFirstName() + " " + request.getLastName()
            );
        }

        return users.stream()
                .filter(user ->
                        request.getPhoneNumber() == null ||
                                user.getPhoneNumber().equals(request.getPhoneNumber())
                        )

                .filter(user ->
                        request.getEmail() == null ||
                                user.getEmail().equalsIgnoreCase(request.getEmail())
                )

                .map(user -> AccountLookupResponse.builder()
                        .userId(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phoneNumber(user.getPhoneNumber())
                        .email(user.getEmail())
                        .accounts(
                                user.getAccounts().stream()
                                        .map(account -> AccountSummaryResponse.builder()
                                                .accountNumber(account.getAccountNumber())
                                                .accountType(account.getAccountType())
                                                .balance(account.getBalance())
                                                .currency(account.getCurrency())
                                                .status(account.getStatus())
                                                .build()
                                        )
                                        .toList()
                        )
                        .build()
                )

                .toList();
    }


    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .address(mapAddressToResponse(user.getAddress()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private AddressResponse mapAddressToResponse(Address address) {

        if (address == null) {
            return null;
        }

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
