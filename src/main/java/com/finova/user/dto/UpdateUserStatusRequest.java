package com.finova.user.dto;

import com.finova.user.roles.UserStatuses;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserStatusRequest {
    @NotNull(message = "User status is required")
    private UserStatuses statuses;
}
