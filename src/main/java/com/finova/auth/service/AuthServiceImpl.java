package com.finova.auth.service;

import com.finova.Notification.service.EmailService;
import com.finova.address.dto.AddressResponse;
import com.finova.auth.dto.*;
import com.finova.auth.model.PasswordResetToken;
import com.finova.auth.model.RefreshToken;
import com.finova.auth.model.Otp;
import com.finova.auth.repository.OtpRepository;
import com.finova.auth.repository.PasswordResetTokenRepository;
import com.finova.auth.repository.RefreshTokenRepository;
import com.finova.autheticatedUser.GetAuthenticatedUser;
import com.finova.common.config.CustomUser;
import com.finova.common.exception.*;
import com.finova.common.jwt.JWTService;
import com.finova.user.dto.UserRequest;
import com.finova.user.dto.UserResponse;
import com.finova.user.model.User;
import com.finova.user.repository.UserRepository;
import com.finova.user.roles.UserRole;
import com.finova.user.roles.UserStatuses;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JWTService jwtService;
    private RefreshTokenRepository refreshTokenRepository;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private EmailService emailService;
    private OtpRepository otpRepository;
    private GetAuthenticatedUser getAuthenticatedUser;

    @Override
    public UserResponse register(UserRequest userRequest) {
        // check for duplicate
        if (userRepository.existsByEmail(userRequest.getEmail())){
            throw  new EmailAlreadyExistsException(userRequest.getEmail());
        }

        if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())){
            throw  new PhoneNumberAlreadyExistsException(userRequest.getPhoneNumber());
        }

        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        // Set Default values controller by the backend
        user.setRole(UserRole.USER);
        user.setStatus(UserStatuses.ACTIVE);

        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();

        response.setId(savedUser.getId());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setEmail(savedUser.getEmail());
        response.setPhoneNumber(savedUser.getPhoneNumber());
        response.setRole(savedUser.getRole());
        response.setStatus(savedUser.getStatus());
        response.setCreatedAt(savedUser.getCreatedAt());
        response.setUpdatedAt(savedUser.getUpdatedAt());

        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }catch (BadCredentialsException e) {
            // Catch any authentication exception and throw custom message
            throw new InvalidCredentialsException("Email or password not correct");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        // Check user status
        if (user.getStatus() == UserStatuses.BLOCKED) {
            throw  new AccountStatusException("User account is blocked");
        }

        if (user.getStatus() == UserStatuses.FROZEN) {
            throw  new AccountStatusException("User account is frozen");
        }
        // Create CustomUser

        CustomUser customUser = new CustomUser(user);

        // Generate access token
        String accessToken = jwtService.generateToken(customUser);

        // Generate refresh token
        String refreshToken = jwtService.generateRefreshToken(customUser);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        // Convert User to UserResponse
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        //Address
        if (user.getAddress() != null) {
            userResponse.setAddress(
                    AddressResponse.builder()
                            .id(user.getAddress().getId())
                            .street(user.getAddress().getStreet())
                            .city(user.getAddress().getStreet())
                            .country(user.getAddress().getCountry())
                            .postalCode(user.getAddress().getPostalCode())
                            .build()
            );
        }


    // build login response
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userResponse)
                .build();
    }

    @Override
    @Transactional
    public LoginResponse refresh(RefreshTokenRequest tokenRequest) {

        // Find refresh token in database
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(tokenRequest.getRefreshToken())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid refresh token")
                );

        // Check if refresh token has been revoked
        if (refreshToken.isRevoked()) {
            throw new InvalidCredentialsException(
                    "Refresh token has been revoked"
            );
        }

        // Check if refresh token has expired
        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidCredentialsException(
                    "Refresh token has expired"
            );
        }

        // Get user from refresh token
        User user = refreshToken.getUser();

        // Check user status
        if (user.getStatus() == UserStatuses.BLOCKED) {
            throw new AccountStatusException(
                    "User account is blocked"
            );
        }

        if (user.getStatus() == UserStatuses.FROZEN) {
            throw new AccountStatusException(
                    "User account is frozen"
            );
        }

        // Create CustomUser
        CustomUser customUser = new CustomUser(user);

        // Validate JWT refresh token
        if (!jwtService.isTokenValid(
                tokenRequest.getRefreshToken(),
                customUser
        )) {
            throw new InvalidCredentialsException(
                    "Invalid or expired refresh token"
            );
        }

        // Generate new access token
        String accessToken = jwtService.generateToken(customUser);

        // Return response
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(tokenRequest.getRefreshToken())
                .user(
                        UserResponse.builder()
                                .id(user.getId())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .phoneNumber(user.getPhoneNumber())
                                .role(user.getRole())
                                .status(user.getStatus())
                                .createdAt(user.getCreatedAt())
                                .updatedAt(user.getUpdatedAt())
                                .build()
                )
                .build();
    }

    @Override
    public void logout(String refreshToken) {

        RefreshToken token =  refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new
                        InvalidCredentialsException("Invalid refresh token")
                );

        token.setRevoked(true);

    }

    @Override
    @Transactional
    public void forgetPassword(String email) {
        System.out.println("FORGOT PASSWORD STARTED: " + email);

        User user = getAuthenticatedUser.getAuthenticatedUser();

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiresAt(Instant.now().plus(13, ChronoUnit.MINUTES))
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                token
        );

    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = passwordResetTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new
                        InvalidCredentialsException("Invalid password reset token"));

        if (resetToken.isUsed()) {
            throw new InvalidCredentialsException(
                    "Password reset token has already been used"
            );
        }

        if (resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidCredentialsException(
                    "Password reset token has expired"
            );
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));

        resetToken.setUsed(true);
    }

    @Override
    @Transactional
    public OtpResponse verifyOtp(VerifyOtpRequest request) {
        /* * Get the currently authenticated user. */

        User user = getAuthenticatedUser.getAuthenticatedUser();

        /* * Find the most recent OTP belonging * to this user and purpose. */

        Otp otp = otpRepository .findTopByUserIdAndPurposeOrderByCreatedAtDesc( user.getId(),
                request.getPurpose() ) .orElseThrow(OtpNotFoundException::new);

        /* * Check whether OTP has already been verified. */

        if (otp.isVerified()) { throw new OtpAlreadyVerifiedException(); }

        /* * Check whether OTP has expired. */
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException();
        }
        /* * Check whether the supplied code matches. */

        if (!otp.getCode().equals(request.getCode()))
        { throw new InvalidOtpException(); }
        /* * OTP is valid. */

        otp.setVerified(true);
        Otp savedOtp = otpRepository.save(otp);

        /* * Convert Entity → Response DTO. */
        return OtpResponse.builder()
                .id(savedOtp.getId())
                .purpose(savedOtp.getPurpose())
                .expiresAt(savedOtp.getExpiresAt())
                .verified(savedOtp.isVerified())
                .createdAt(savedOtp.getCreatedAt())
                .build();
    }
    }





