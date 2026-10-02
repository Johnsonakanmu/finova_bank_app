package com.finova.customer.specification;

import com.finova.user.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;

@Component
public class UserSpecification {

    public static Specification<User> searchCustomers(
            String firstName,
            String lastName,
            String email,
            String phoneNumber
    ){

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (firstName != null && !firstName.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("firstName")),
                                "%" + firstName.toLowerCase() + "%"
                        )
                );
            }

            if (lastName != null && !lastName.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("lastName")),
                                "%" + lastName.toLowerCase() + "%"
                        )
                );
            }

            if (email != null && !email.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("email")),
                                email.toLowerCase()
                        )
                );
            }

            if (phoneNumber != null && !phoneNumber.isBlank()) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("phoneNumber"),
                                phoneNumber
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }


}



