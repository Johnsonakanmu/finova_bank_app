package com.finova.customer.service;

import com.finova.customer.dto.InternalCustomerResponse;
import com.finova.user.model.User;
import com.finova.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InternalCustomerServiceImpl implements InternalCustomerService{

    private final UserRepository userRepository;

    @Override
    public List<InternalCustomerResponse> searchCustomers(String firstName, String lastName,
                                                          String email, String phoneNumber) {
        List<User> users = userRepository.searchCustomers(
                firstName,
                lastName,
                email,
                phoneNumber
        );

        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public InternalCustomerResponse getCustomerById(Long id) {
        return null;
    }

    private InternalCustomerResponse mapToResponse(User user) {

        return InternalCustomerResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }


}





