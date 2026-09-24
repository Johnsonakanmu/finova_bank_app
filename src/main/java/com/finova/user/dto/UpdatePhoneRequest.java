package com.finova.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePhoneRequest {

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+234[7-9][0-9]{9}$",
            message = "Enter a valid Nigerian phone number in international format"
    )
    private String phoneNumber;
}
