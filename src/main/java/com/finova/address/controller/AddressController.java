package com.finova.address.controller;

import com.finova.address.dto.AddressRequest;
import com.finova.address.dto.AddressResponse;
import com.finova.address.service.AddressService;
import com.finova.apiResponse.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Address",
        description ="CRUD REST APIs for Finova Resource"
)

@RestController
@RequestMapping("/api/users/me/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> createAddress(
            @Valid @RequestBody AddressRequest request
    ) {

        AddressResponse response =
                addressService.createAddress(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                "Address created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AddressResponse>> getMyAddress() {

        AddressResponse response =
                addressService.getMyAddress();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Address retrieved successfully",
                        response
                )
        );
    }


    @PutMapping
    public ResponseEntity<ApiResponse<AddressResponse>> updateMyAddress(
            @Valid @RequestBody AddressRequest request
    ) {

        AddressResponse response =
                addressService.updateMyAddress(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Address updated successfully",
                        response
                )
        );
    }


    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteMyAddress() {

        addressService.deleteMyAddress();

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Address deleted successfully",
                        null
                )
        );
    }

}
