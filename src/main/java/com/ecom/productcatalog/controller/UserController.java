package com.ecom.productcatalog.controller;

import com.ecom.productcatalog.dto.UserResponse;
import com.ecom.productcatalog.entity.User;
import com.ecom.productcatalog.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;    //  Repository is used to find the user from the database.

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow();

        return UserResponse.from(user);
    }
}
