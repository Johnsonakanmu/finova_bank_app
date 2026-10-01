package com.finova.transaction.controller;

import com.finova.apiResponse.ApiResponse;
import com.finova.transaction.dto.*;
import com.finova.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(
        name = "Transaction",
        description ="CRUD REST APIs for managing user transaction in the Finova application"
)

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/me/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(
            summary = "Deposit Money",
            description = "Deposits money into the authenticated user's bank account and updates the account balance."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Money deposited successfully."
    )

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @Valid @RequestBody DepositRequest request
    ) {

        TransactionResponse response =
                transactionService.deposit(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Deposit successful",
                                response
                        )
                );
    }

    @Operation(
            summary = "Withdraw Money",
            description = "Withdraws money from the authenticated user's bank account and updates the account balance."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Money withdrawn successfully."
    )
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @Valid @RequestBody WithdrawRequest request
    ) {

        TransactionResponse response =
                transactionService.withdraw(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Withdrawal successful",
                                response
                        )
                );
    }

    @Operation(
            summary = "Transfer Money",
            description = "Transfers money from the authenticated user's account to another bank account and updates the affected account balances."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Money transferred successfully."
    )

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(
            @Valid @RequestBody TransferRequest request
    ) {

        TransactionResponse response =
                transactionService.transfer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Transfer successful",
                                response
                        )
                );
    }

    @Operation(
            summary = "Get Statement Account",
            description = "Get authenticated statement account."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account frozen successfully."
    )
    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<ApiResponse<StatementResponse>> getStatement(
          @PathVariable String accountNumber,
           @RequestParam LocalDate from,
           @RequestParam LocalDate to
    ) {
        StatementResponse response = transactionService.getStatement(accountNumber, from, to);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                true,
                                "Statement retrieved successfully",
                                response
                        )
                );
    }

}
