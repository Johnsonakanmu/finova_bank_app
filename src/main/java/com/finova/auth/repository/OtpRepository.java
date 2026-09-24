package com.finova.auth.repository;

import com.finova.auth.model.Otp;
import com.finova.auth.roles.OTPPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findTopByUserIdAndPurposeOrderByCreatedAtDesc(
            Long userId, OTPPurpose purpose
    );

}
