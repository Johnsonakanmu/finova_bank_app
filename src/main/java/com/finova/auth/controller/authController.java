package com.finova.auth.controller;

import com.finova.apiResponse.ApiResponse;
import com.finova.auth.dto.*;
import com.finova.auth.service.AuthService;
import com.finova.user.dto.UserRequest;
import com.finova.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Auth",
        description ="CRUD REST APIs for Finova Resource"
)

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class authController {

    private final AuthService authService;


    // for Swagger implementation For POST
    @Operation(
            summary = "Create register user Rest API",
            description = "Register user Rest API is used to create a user in the database"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    //
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody @Valid UserRequest userRequest) {

        UserResponse response = authService.register(userRequest);

        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(
                true,
                "User registered successfully",
                response
        );

        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponse);
    }



    // for Swagger implementation For POST
    @Operation(
            summary = "Login User Rest API",
            description = "Login User Rest API is used to login in a user"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    //
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest userRequest) {

        LoginResponse response = authService.login(userRequest);

        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(
                true,
                "Logged in successfully",
                response
        );

        return  ResponseEntity.status(HttpStatus.OK)
                .body(apiResponse);

    }



    // for Swagger implementation For POST
    @Operation(
            summary = "Refresh token Rest API",
            description = "Refresh user token if the user want to refresh it"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    //
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@RequestBody RefreshTokenRequest tokenRequest) {

        LoginResponse response = authService.refresh(tokenRequest);

        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(
                true,
                "Refresh successfully",
                response
        );

        return  ResponseEntity.status(HttpStatus.OK)
                .body(apiResponse);
    }



    // for Swagger implementation For POST
    @Operation(
            summary = "Logout Rest API",
            description = "User can logout anytime when he/she is done"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    //
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Valid RefreshTokenRequest request) {



        authService.logout(request.getRefreshToken());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Logout scuccessfl",
                        null
                )
        );
    }



    // for Swagger implementation For POST
    @Operation(
            summary = "Forgot Password Rest API",
            description = "User forgot is password he/she can change it"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    //
    @PostMapping("/forgot_password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request
    ) {

        authService.forgetPassword(request.getEmail());

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Password reset instructions have been sent to your email",
                        null
                )
        );
    }



    // for Swagger implementation For POST
    @Operation(
            summary = "Reset Password Rest API",
            description = "To reset ur password when u think someone else now ur password"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    //
    @PostMapping("/reset_password")
    public ResponseEntity<ApiResponse<Void>> restPassword(@RequestBody ResetPasswordRequest request) {

        authService.resetPassword(
                request.getToken(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Password has been reset successfully",
                        null
                )
        );

    }


    // for Swagger implementation For POST
    @Operation(
            summary = "Verify OTP  Rest API",
            description = "To Verify the OTP if is correct or not"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    //
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<OtpResponse>> verifyOtp(@RequestBody @Valid VerifyOtpRequest request) {

        OtpResponse response = authService.verifyOtp(request);

        return  ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                true,
                                "OTP verified successfully",
                                response
                        )
                );
    }


    @PostMapping("/verify-phone")
    public ResponseEntity<LoginResponse> verifyPhone(@RequestBody LoginRequest userRequest) {

        return  null;
    }

    @PostMapping("/resend-phone-otp")
    public ResponseEntity<LoginResponse> resendPhoneOtp(@RequestBody LoginRequest userRequest) {

        return  null;
    }

}
