package com.finova.beneficiary.controller;

import com.finova.apiResponse.ApiResponse;
import com.finova.beneficiary.dto.BeneficiaryRequest;
import com.finova.beneficiary.dto.BeneficiaryResponse;
import com.finova.beneficiary.service.BeneficiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Beneficiary",
        description ="CRUD REST APIs for managing beneficiary accounts in the Finova application"
)

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/beneficiary")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    // for Swagger implementation For POST
    @Operation(
            summary = "Create Beneficiary Account",
            description = "Creates beneficiary account for the authenticated user and saves the account details to the database."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Bank account created successfully."
    )
    //

    @PostMapping
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> createBeneficiary(@RequestBody
                                                                              BeneficiaryRequest request) {

        BeneficiaryResponse response = beneficiaryService.createBeneficiary(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Beneficiary Created Successfully",
                                response
                        )
                );
    }


    // for Swagger implementation For GET all User
    @Operation(
            summary = "Get All Beneficiary",
            description = "Retrieves all beneficiary from the database."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully."
    )
    //

    @GetMapping
    public ResponseEntity<ApiResponse<List<BeneficiaryResponse>>> getBeneficiaries(){

       List<BeneficiaryResponse> response = beneficiaryService.getMyBeneficiaries();

       return ResponseEntity.status(HttpStatus.OK)
               .body(
                       new ApiResponse<>(
                               true,
                               "Beneficiaries retrieved successfully",
                               response
                       )
               );
    }



    // for Swagger implementation For GET all User
    @Operation(
            summary = "Get Beneficiary",
            description = "Retrieves a beneficiary from the database."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully."
    )
    //
    @GetMapping("/id")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> getBeneficiary(@PathVariable("id") Long id){

        BeneficiaryResponse response = beneficiaryService.getMyBeneficiary(id);

        return ResponseEntity.ok(
                        new ApiResponse<>(
                                true,
                                "Beneficiaries retrieved successfully",
                                response
                        )
                );
    }

    @Operation(
            summary = "Update Beneficiary Account",
            description = "Update beneficiary who is authenticated user's account."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account frozen successfully."
    )

    @PutMapping("/id")
    public ResponseEntity<ApiResponse<BeneficiaryResponse>> updateBeneficiary(
            @PathVariable Long id,
            @Valid @RequestBody BeneficiaryRequest request
    ){

        BeneficiaryResponse response = beneficiaryService.updateBeneficiary(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Beneficiaries retrieved successfully",
                        response
                )
        );

    }

    @Operation(
            summary = "Delete Beneficiary Account",
            description = "Delete authenticated beneficiary account."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account frozen successfully."
    )

    @DeleteMapping("/id")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        beneficiaryService.deleteBeneficiary(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Beneficiaries retrieved successfully",
                        null
                )
        );
    }



}
