package com.finova.account.controller;

import com.finova.account.dto.InternalAccountResponse;
import com.finova.account.service.InternalAccountService;
import com.finova.apiResponse.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Account",
        description = "CRUD REST APIs for managing user accounts in the Finova application"
)

@RestController
@RequestMapping("/api/internal/customers")
@RequiredArgsConstructor
public class InternalAccountController {

    private final InternalAccountService internalAccountService;

    @GetMapping("/{customerId}/accounts")
    public ResponseEntity<ApiResponse<List<InternalAccountResponse>>>
    getCustomerAccounts(@PathVariable("id") Long customerId ) {

        List<InternalAccountResponse> accounts =internalAccountService.getCustomerAccounts(customerId);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Customer account retrieved successfully",
                        accounts
                )
        );

    }

    @GetMapping("/{customerId}/accounts/{accountNumber}")
    public ResponseEntity<ApiResponse<InternalAccountResponse>> getCustomerAccount(
            @PathVariable Long customerId,
            @PathVariable String accountNumber
    ){

        InternalAccountResponse account = internalAccountService.getCustomerAccount(
                customerId, accountNumber
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Customer account retrieved successfully",
                        account
                )
        );
    }
}
