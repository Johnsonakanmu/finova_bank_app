package com.finova.beneficiary.service;

import com.finova.beneficiary.dto.BeneficiaryRequest;
import com.finova.beneficiary.dto.BeneficiaryResponse;

import java.util.List;


public interface BeneficiaryService  {

    public BeneficiaryResponse createBeneficiary(BeneficiaryRequest request);

    public List<BeneficiaryResponse> getMyBeneficiaries();

    public BeneficiaryResponse getMyBeneficiary(Long id);

    public BeneficiaryResponse updateBeneficiary( Long id, BeneficiaryRequest request);

    void deleteBeneficiary(Long id);
}
