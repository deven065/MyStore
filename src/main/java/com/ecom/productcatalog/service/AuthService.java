package com.ecom.productcatalog.service;

import com.ecom.productcatalog.dto.LoginRequest;
import com.ecom.productcatalog.dto.LoginResponse;
import com.ecom.productcatalog.dto.SignupRequest;
import com.ecom.productcatalog.dto.UserResponse;
import com.ecom.productcatalog.entity.Role;
import com.ecom.productcatalog.entity.User;
import com.ecom.productcatalog.exception.EmailAlreadyExistsException;
import com.ecom.productcatalog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password and confirm password do not match"
            );
        }

        String normalizedEmail = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new EmailAlreadyExistsException(
                    "An account with this email already exists"
            );
        }

        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
        user.setRole(Role.CUSTOMER);
        user.setEnabled(true);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        return UserResponse.from(savedUser);
    }

    public LoginResponse login(LoginRequest request) {
        String normalizedEmail = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }
        return new LoginResponse(
                "Login Successful",
                UserResponse.from(user)
        );
    }
}
