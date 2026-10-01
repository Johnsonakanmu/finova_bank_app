package com.finova.user.repository;

import com.finova.user.model.User;
import com.finova.user.roles.UserStatuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


    @Query("""
    SELECT u FROM User u
    WHERE (:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
      AND (:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
      AND (:email IS NULL OR LOWER(u.email) = LOWER(:email))
      AND (:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber)
""")
    List<User> searchCustomers(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("email") String email,
            @Param("phoneNumber") String phoneNumber
    );

}
