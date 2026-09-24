package com.finova.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountLookupResponse {
    private Long userId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private List<AccountSummaryResponse> accounts;
}
