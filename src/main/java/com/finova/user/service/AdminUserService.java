package com.finova.user.service;

import com.finova.user.dto.AccountLookupRequest;
import com.finova.user.dto.AccountLookupResponse;
import com.finova.user.dto.UpdateUserStatusRequest;
import com.finova.user.dto.UserResponse;
import com.finova.user.model.User;
import com.finova.user.roles.UserStatuses;

import java.util.List;

public interface AdminUserService {

    List<UserResponse> getAllUsers(UserStatuses statuses);

    UserResponse getUserById(Long id);

    UserResponse updateUserStatus(Long id, UpdateUserStatusRequest request);

    void deleteUser(Long id);

    List<AccountLookupResponse> findCustomers(AccountLookupRequest request);

}
