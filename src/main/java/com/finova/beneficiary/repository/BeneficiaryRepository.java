package com.finova.beneficiary.repository;

import com.finova.beneficiary.model.Beneficiary;
import com.finova.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByUser(User user);

    Optional<Beneficiary> findByIdAndUser(Long id, User user);
    boolean existsByUserAndAccountNumber(User user, String accountNumber);
}
