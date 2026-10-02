package com.finova.customer.service;

import com.finova.customer.dto.InternalCustomerResponse;
import com.finova.customer.repository.UserRepositories;
import com.finova.customer.specification.UserSpecification;
import com.finova.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InternalCustomerServiceImpl implements InternalCustomerService{

    private UserRepositories userRepositories;

    @Override
    public List<InternalCustomerResponse> searchCustomers(
            String firstName,
            String lastName,
            String email,
            String phoneNumber
    ) {

        Specification<User> specification =
                UserSpecification.searchCustomers(
                        firstName,
                        lastName,
                        email,
                        phoneNumber
                );

        List<User> users = userRepositories.findAll(specification);

        return users.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public InternalCustomerResponse getCustomerById(Long id) {

        User user = userRepositories.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id: " + id
                        )
                );

        return mapToResponse(user);
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
