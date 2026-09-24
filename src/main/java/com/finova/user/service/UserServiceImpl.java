package com.finova.user.service;

import com.finova.address.dto.AddressRequest;
import com.finova.address.dto.AddressResponse;
import com.finova.address.model.Address;
import com.finova.common.exception.EmailAlreadyExistsException;
import com.finova.common.exception.InvalidPasswordException;
import com.finova.common.exception.PhoneNumberAlreadyExistsException;
import com.finova.common.exception.ResourceNotFoundException;
import com.finova.user.dto.*;
import com.finova.user.model.User;
import com.finova.user.repository.UserRepository;
import com.finova.user.roles.UserStatuses;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new RuntimeException("User is not authenticated");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "email",
                                email
                        ));
    }

    @Override
    @Transactional
    public UserResponse getCurrentUser() {

        User user = getAuthenticatedUser();
        return mapToResponse(user);
    }

    @Override
    public UserResponse updateCurrentUser(UserRequest request) {
        User user = getAuthenticatedUser();

        // Check email only if it has changed
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {

            throw new EmailAlreadyExistsException(
                    request.getEmail()
            );
        }

        // Check phone only if it has changed
        if (!user.getPhoneNumber().equals(request.getPhoneNumber())
                && userRepository.existsByPhoneNumber(
                request.getPhoneNumber())) {

            throw new PhoneNumberAlreadyExistsException(
                    request.getPhoneNumber()
            );
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        /*
         * Don't update the password here.
         *
         * Password should have its own endpoint:
         * PATCH /api/users/me/password
         */

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    public void changePassword(UpdatePasswordRequest request) {

        User user = getAuthenticatedUser();

        // Check current password
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new InvalidPasswordException(
                    "Current password is incorrect"
            );
        }

        // Prevent using the same password
        if (passwordEncoder.matches(
                request.getNewPassword(),
                user.getPassword())) {

            throw new InvalidPasswordException(
                    "New password must be different from current password"
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

    }

    @Override
    public UserResponse updatePhone(UpdatePhoneRequest request) {
        User user = getAuthenticatedUser();

        if (user.getPhoneNumber().equals(request.getPhoneNumber())) {
            return mapToResponse(user);
        }

        if (userRepository.existsByPhoneNumber(
                request.getPhoneNumber())) {

            throw new PhoneNumberAlreadyExistsException(
                    request.getPhoneNumber()
            );
        }

        user.setPhoneNumber(request.getPhoneNumber());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse updateEmail(UpdateEmailRequest request) {
        User user = getAuthenticatedUser();

        if (user.getEmail().equalsIgnoreCase(request.getEmail())) {
            return mapToResponse(user);
        }

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new EmailAlreadyExistsException(
                    request.getEmail()
            );
        }

        user.setEmail(request.getEmail());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse updateAddress(AddressRequest request) {
        User user = getAuthenticatedUser();

        Address address = user.getAddress();

        if (address == null) {

            address = new Address();

            user.setAddress(address);
        }

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setCountry(request.getCountry());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    public void deleteCurrentUser() {

        User user = getAuthenticatedUser();

        /*
         * For a banking system, don't physically delete
         * the user because accounts and transactions
         * need to remain available for audit purposes.
         *
         * Instead, deactivate/block the account.
         */

        user.setStatus(UserStatuses.BLOCKED);

        userRepository.save(user);

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
