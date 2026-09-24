package com.finova.user.controller;

import com.finova.apiResponse.ApiResponse;
import com.finova.user.dto.AccountLookupRequest;
import com.finova.user.dto.AccountLookupResponse;
import com.finova.user.dto.UpdateUserStatusRequest;
import com.finova.user.dto.UserResponse;
import com.finova.user.roles.UserStatuses;
import com.finova.user.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Admin",
        description ="CRUD REST APIs for managing administrators in the Finova application"
)

@RestController
@RequestMapping("/api/admin/users")
@AllArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(
            summary = "Get all user",
            description = "Retrieves all authenticated user's by admin."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully."
    )

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @RequestParam(required = false) UserStatuses statuses
    ) {

        List<UserResponse> users =
                adminUserService.getAllUsers(statuses);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Users retrieved successfully",
                        users
                )
        );
    }

    @Operation(
            summary = "Get Account by Customer Name",
            description = "Retrieves the account associated with the specified customer name by admin."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully."
    )

    @GetMapping("/account-lookup")
    public ResponseEntity<ApiResponse<List<AccountLookupResponse>>> findCustomer(
            @Valid AccountLookupRequest request
            ) {
        List<AccountLookupResponse> response =
        adminUserService.findCustomers(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Customer account information retrieved successfully",
                        response
                )
        );
    }

    @Operation(
            summary = "Get User by ID",
            description = "Retrieves the user associated with the specified user ID by admin."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User retrieved successfully."
    )

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(
            @PathVariable Long id
    ) {

        UserResponse user =
                adminUserService.getUserById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User retrieved successfully",
                        user
                )
        );
    }


    @Operation(
            summary = "Update User Account",
            description = "Update authenticated user's by admin."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account frozen successfully."
    )

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {

        UserResponse user =
                adminUserService.updateUserStatus(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User status updated successfully",
                        user
                )
        );
    }

    @Operation(
            summary = "Delete User Account",
            description = "Delete authenticated user account by admin."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account frozen successfully."
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id
    ) {

        adminUserService.deleteUser(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User deactivated successfully",
                        null
                )
        );
    }
}
