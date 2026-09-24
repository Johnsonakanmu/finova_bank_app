package com.finova.beneficiary.service;

import com.finova.autheticatedUser.GetAuthenticatedUser;
import com.finova.beneficiary.dto.BeneficiaryRequest;
import com.finova.beneficiary.dto.BeneficiaryResponse;
import com.finova.beneficiary.mapper.BeneficiaryMapper;
import com.finova.beneficiary.model.Beneficiary;
import com.finova.beneficiary.repository.BeneficiaryRepository;
import com.finova.common.exception.ResourceNotFoundException;
import com.finova.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryServiceImpl implements BeneficiaryService{

    private final BeneficiaryRepository beneficiaryRepository;
    private final GetAuthenticatedUser getAuthenticatedUser;
    private final BeneficiaryMapper beneficiaryMapper;

    @Override
    @Transactional
    public BeneficiaryResponse createBeneficiary(BeneficiaryRequest request) {

        User user = getAuthenticatedUser.getAuthenticatedUser();

        // Prevent duplicate Beneficiary

        if (beneficiaryRepository.existsByUserAndAccountNumber(
                user, request.getAccountNumber()
        )){
            throw  new IllegalArgumentException(
                    "Beneficiary with account number "
                            + request.getAccountNumber()
                            + " already exists"
            );
        }

        // Prevent user from adding own account
        boolean ownAccount = user.getAccounts()
                .stream().anyMatch(account ->
                        account.getAccountNumber()
                                .equals(request.getAccountNumber())
                );

        if (ownAccount) {
            throw new IllegalArgumentException(
                    "You cannot add your own account as a beneficiary"
            );
        }

        Beneficiary beneficiary = Beneficiary.builder()
                .name(request.getName())
                .accountNumber(request.getAccountNumber())
                .bankName(request.getBankName())
                .user(user)
                .build();

        beneficiary = beneficiaryRepository.save(beneficiary);

        return beneficiaryMapper.mapToResponse(beneficiary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeneficiaryResponse> getMyBeneficiaries() {

        User user = getAuthenticatedUser.getAuthenticatedUser();

        return beneficiaryRepository.findByUser(user)
                .stream()
                .map(beneficiaryMapper::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryResponse getMyBeneficiary(Long id) {

        User user = getAuthenticatedUser.getAuthenticatedUser();

        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUser(
                id, user
        ).orElseThrow(() ->
                new ResourceNotFoundException("Beneficiary", "id", id.toString())
                );

        return beneficiaryMapper.mapToResponse(beneficiary);
    }

    @Override
    @Transactional(readOnly = true)
    public BeneficiaryResponse updateBeneficiary(Long id, BeneficiaryRequest request) {

        User user = getAuthenticatedUser.getAuthenticatedUser();

        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUser(
                id, user
        ).orElseThrow(() ->
                new ResourceNotFoundException("Beneficiary", "id", id.toString())
        );

        // If account number is being changed,
        // make sure another beneficiary doesn't already use it.

        if (!beneficiary.getAccountNumber()
                .equals(request.getAccountNumber())
                && beneficiaryRepository.existsByUserAndAccountNumber(
                user,
                request.getAccountNumber()
        )) {

            throw new IllegalArgumentException(
                    "Beneficiary with account number "
                            + request.getAccountNumber()
                            + " already exists"
            );
        }

        beneficiary.setName(request.getName());
        beneficiary.setAccountNumber(request.getAccountNumber());
        beneficiary.setBankName(request.getBankName());

        beneficiary = beneficiaryRepository.save(beneficiary);

        return beneficiaryMapper.mapToResponse(beneficiary);
    }

    @Override
    @Transactional
    public void deleteBeneficiary(Long id) {

        User user = getAuthenticatedUser.getAuthenticatedUser();

        Beneficiary beneficiary = beneficiaryRepository.findByIdAndUser(
                id, user
        ).orElseThrow(() ->
                new ResourceNotFoundException("Beneficiary", "id", id.toString())
        );

        beneficiaryRepository.delete(beneficiary);
    }

}
