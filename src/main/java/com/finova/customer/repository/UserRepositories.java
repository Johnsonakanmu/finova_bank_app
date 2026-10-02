package com.finova.customer.repository;

import com.finova.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepositories extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
