package com.ecom.productcatalog.dto;

import com.ecom.productcatalog.entity.Role;
import com.ecom.productcatalog.entity.User;

import java.time.Instant;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role,
        boolean emailVerified,
        Instant createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isEmailVerified(),
                user.getCreatedAt()
        );
    }
}

//  passwordHash, enabled and updatedAt is not written because password hashes should never be included in an API response.