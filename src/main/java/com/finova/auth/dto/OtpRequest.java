package com.finova.auth.dto;

import com.finova.auth.roles.OTPPurpose;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OtpRequest {
    @NotNull(message = "OTP purpose is required")
    private OTPPurpose purpose;
}
