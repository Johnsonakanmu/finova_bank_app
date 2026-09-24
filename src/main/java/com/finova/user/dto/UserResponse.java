package com.finova.user.dto;

import com.finova.address.dto.AddressResponse;
import com.finova.user.roles.UserRole;
import com.finova.user.roles.UserStatuses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private UserStatuses status;
    private AddressResponse address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}


