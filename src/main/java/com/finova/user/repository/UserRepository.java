package com.finova.user.repository;

import com.finova.user.model.User;
import com.finova.user.roles.UserStatuses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    List<User> findByStatus(UserStatuses status);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );
}
