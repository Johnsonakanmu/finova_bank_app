package com.finova.user.controller;

import com.finova.apiResponse.ApiResponse;
import com.finova.customer.dto.InternalCustomerResponse;
import com.finova.customer.service.InternalCustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Customer",
        description ="CRUD REST APIs for managing Customer in the Finova application"
)

@RestController
@RequestMapping("/api/internal/customers")
@RequiredArgsConstructor
public class InternalCustomerController {

    private final InternalCustomerService internalCustomerService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<InternalCustomerResponse>>> searchCustomers(

            @RequestParam(required = false)
            String firstName,

            @RequestParam(required = false)
            String lastName,

            @RequestParam(required = false)
            String email,

            @RequestParam(required = false)
            String phoneNumber
    ) {

        List<InternalCustomerResponse> customers =
                internalCustomerService.searchCustomers(
                        firstName,
                        lastName,
                        email,
                        phoneNumber
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Customers retrieved successfully",
                        customers
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InternalCustomerResponse>> getCustomerById(
            @PathVariable Long id
    ) {

        InternalCustomerResponse customer =
                internalCustomerService.getCustomerById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Customer retrieved successfully",
                        customer
                )
        );
    }
}
