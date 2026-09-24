package com.finova.user.model;

import com.finova.account.model.Account;
import com.finova.address.model.Address;
import com.finova.beneficiary.model.Beneficiary;
import com.finova.user.roles.UserStatuses;
import com.finova.user.roles.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    @Column(name = "phone_number", nullable = false,
            unique = true, length = 14)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Enumerated(EnumType.STRING)
    private UserStatuses status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany( mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Account> accounts = new ArrayList<>();

    @OneToMany( mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    @Builder.Default
    private List<Beneficiary> beneficiaries = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
