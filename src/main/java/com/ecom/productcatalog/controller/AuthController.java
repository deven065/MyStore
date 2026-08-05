package com.ecom.productcatalog.controller;

import com.ecom.productcatalog.dto.LoginRequest;
import com.ecom.productcatalog.dto.LoginResponse;
import com.ecom.productcatalog.dto.SignupRequest;
import com.ecom.productcatalog.dto.UserResponse;
import com.ecom.productcatalog.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public UserResponse signip(
            @Valid
            @RequestBody
            SignupRequest request
    ) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
