package com.finova.auth.dto;

import com.finova.user.dto.UserResponse;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private UserResponse user;
}
