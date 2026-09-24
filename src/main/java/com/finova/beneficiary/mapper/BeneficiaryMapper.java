package com.finova.beneficiary.mapper;

import com.finova.beneficiary.dto.BeneficiaryResponse;
import com.finova.beneficiary.model.Beneficiary;
import org.springframework.stereotype.Component;

@Component
public class BeneficiaryMapper {

    public BeneficiaryResponse mapToResponse(
            Beneficiary beneficiary
    ) {

        return BeneficiaryResponse.builder()
                .id(beneficiary.getId())
                .name(beneficiary.getName())
                .accountNumber(beneficiary.getAccountNumber())
                .bankName(beneficiary.getBankName())
                .userId(beneficiary.getUser().getId())
                .createdAt(beneficiary.getCreatedAt())
                .build();
    }
}
