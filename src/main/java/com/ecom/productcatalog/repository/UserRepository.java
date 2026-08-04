package com.ecom.productcatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoranceCase(String email);

    Optional<User> findEmailIgnoranceCase(String email);
}
