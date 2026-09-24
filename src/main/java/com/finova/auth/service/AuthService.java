package com.finova.auth.service;

import com.finova.auth.dto.*;
import com.finova.user.dto.UserRequest;
import com.finova.user.dto.UserResponse;

public interface AuthService {

    public UserResponse register(UserRequest userRequest);

    public LoginResponse login(LoginRequest request);

    public LoginResponse refresh(RefreshTokenRequest tokenRequest);

    void logout(String refreshToken);

    void forgetPassword(String email);
    void resetPassword(String token, String newPassword);
    public OtpResponse verifyOtp(VerifyOtpRequest request);

}
