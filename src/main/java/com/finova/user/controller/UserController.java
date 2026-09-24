package com.finova.user.controller;

import com.finova.address.dto.AddressRequest;
import com.finova.apiResponse.ApiResponse;
import com.finova.user.dto.*;
import com.finova.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "User",
        description ="CRUD REST APIs for managing user in the Finova application"
)

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @Operation(
            summary = "Get User",
            description = "Retrieves a user from the database."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully."
    )

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {

        UserResponse response =
                userService.getCurrentUser();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User profile retrieved successfully",
                        response
                )
        );
    }

    @Operation(
            summary = "Update user",
            description = "Update authenticate user in from the database."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Account retrieved successfully."
    )

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateCurrentUser(
            @Valid @RequestBody UserRequest request
    ) {

        UserResponse response =
                userService.updateCurrentUser(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User profile updated successfully",
                        response
                )
        );
    }

    @Operation(
            summary = "Update Password",
            description = "Updates the authenticated user's password after validating the current password."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Password updated successfully."
    )

    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody UpdatePasswordRequest request
    ) {

        userService.changePassword(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Password changed successfully",
                        null
                )
        );
    }

    @Operation(
            summary = "Update Phone",
            description = "Updates the authenticated user's phone."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Password updated successfully."
    )

    @PatchMapping("/me/phone")
    public ResponseEntity<ApiResponse<UserResponse>> updatePhone(
            @Valid @RequestBody UpdatePhoneRequest request
    ) {

        UserResponse response =
                userService.updatePhone(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Phone number updated successfully",
                        response
                )
        );
    }

    @Operation(
            summary = "Update Email",
            description = "Updates the authenticated user's email."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Password updated successfully."
    )

    @PatchMapping("/me/email")
    public ResponseEntity<ApiResponse<UserResponse>> updateEmail(
            @Valid @RequestBody UpdateEmailRequest request
    ) {

        UserResponse response =
                userService.updateEmail(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Email updated successfully",
                        response
                )
        );
    }

    @Operation(
            summary = "Update Address",
            description = "Updates the authenticated address."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Password updated successfully."
    )
    @PatchMapping("/me/address")
    public ResponseEntity<ApiResponse<UserResponse>> updateAddress(
            @Valid @RequestBody AddressRequest request
    ) {

        UserResponse response =
                userService.updateAddress(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Address updated successfully",
                        response
                )
        );
    }


    @Operation(
            summary = "Delete User",
            description = "Delete the authenticated user."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Password updated successfully."
    )
    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteCurrentUser() {

        userService.deleteCurrentUser();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "User account deactivated successfully",
                        null
                )
        );
    }


}
