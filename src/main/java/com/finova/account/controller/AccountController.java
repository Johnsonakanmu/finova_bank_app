package com.finova.account.controller;

import com.finova.account.dto.AccountRequest;
import com.finova.account.dto.AccountResponse;
import com.finova.account.dto.BalanceResponse;
import com.finova.account.service.AccountService;
import com.finova.apiResponse.ApiResponse;
import com.finova.transaction.dto.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Account",
        description = "CRUD REST APIs for managing user accounts in the Finova application"
)

@RestController
@RequestMapping("/api/users/me/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // for Swagger implementation For POST
    @Operation(
            summary = "Create Bank Account",
            description = "Creates a new bank account for the authenticated user and saves the account details to the database."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Bank account created successfully."
    )
    //
    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@RequestBody @Valid AccountRequest request){

        AccountResponse response = accountService.createAccount(request);

        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Account Created Successfully",
                                response
                        )
                );
    }

    // for Swagger implementation For GET all User
    @Operation(
            summary = "Get Account",
            description = "Retrieves an account from the database."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully."
    )
    //

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getMyAccounts() {

        List<AccountResponse> accounts =
                accountService.getAccount();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Accounts retrieved successfully",
                        accounts
                )
        );
    }

    // for Swagger implementation For GET all User
    @Operation(
            summary = "Get My Account by Account Number",
            description = "Retrieves the authenticated user's account using the specified account number."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully."
    )
    //

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountResponse>> getMyAccount(
            @PathVariable String accountNumber
    ) {

        AccountResponse response =
                accountService.getMyAccount(accountNumber);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Account retrieved successfully",
                        response
                )
        );
    }

    // for Swagger implementation For GET all User
    @Operation(
            summary = "Freeze My Account",
            description = "Freezes the authenticated user's account."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account frozen successfully."
    )
    //

    @PatchMapping("/{accountNumber}/freeze")
    public ResponseEntity<ApiResponse<Void>> freezeMyAccount(
            @PathVariable String accountNumber
    ) {

        accountService.freezeMyAccount(accountNumber);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Account frozen successfully",
                        null
                )
        );
    }

    // for Swagger implementation For GET all User
    @Operation(
            summary = "Get All User Transactions",
            description = "Retrieves all transactions associated with the authenticated user from the database."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Transactions retrieved successfully."
    )
    //
    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>>
    getMyAccountTransactions(
            @PathVariable String accountNumber
    ) {

        List<TransactionResponse> transactions =
                accountService.getMyAccountTransactions(accountNumber);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Account transactions retrieved successfully",
                        transactions
                )
        );
    }

    // for Swagger implementation For GET all User
    @Operation(
            summary = "Get Balance Account",
            description = "Get balance of a authenticated user's account."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account frozen successfully."
    )
    //

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<ApiResponse<BalanceResponse>> getBalance(
            @PathVariable String accountNumber
    ) {

        BalanceResponse response =
                accountService.getBalance(accountNumber);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Account balance retrieved successfully",
                        response
                )
        );
    }
}
