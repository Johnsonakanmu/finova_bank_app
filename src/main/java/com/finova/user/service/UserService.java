package com.finova.user.service;

import com.finova.address.dto.AddressRequest;
import com.finova.address.dto.AddressResponse;
import com.finova.user.dto.*;

public interface UserService {

    UserResponse getCurrentUser();

    UserResponse updateCurrentUser(UserRequest request);

    void changePassword(UpdatePasswordRequest request);

    UserResponse updatePhone(UpdatePhoneRequest request);

    UserResponse updateEmail(UpdateEmailRequest request);

    UserResponse updateAddress(AddressRequest request);


    void deleteCurrentUser();
}
