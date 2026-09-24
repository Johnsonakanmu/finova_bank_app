package com.finova.beneficiary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryResponse {

    private Long id;

    private String name;

    private String accountNumber;

    private String bankName;

    private Long userId;

    private LocalDateTime createdAt;
}


