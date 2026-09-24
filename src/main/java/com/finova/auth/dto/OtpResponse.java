package com.finova.auth.dto;

import com.finova.auth.roles.OTPPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponse {

    private Long id;

    private OTPPurpose purpose;

    private LocalDateTime expiresAt;

    private boolean verified;

    private LocalDateTime createdAt;
}


