package com.akumatech.backend.auth.controller;

import com.akumatech.backend.auth.dto.AuthResponse;
import com.akumatech.backend.auth.dto.LoginRequest;
import com.akumatech.backend.auth.dto.RegisterRequest;
import com.akumatech.backend.auth.service.AuthService;
import com.akumatech.backend.user.User;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return auth.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return auth.login(request);
    }

    @GetMapping("/me")
    public AuthResponse me(@AuthenticationPrincipal User user) {
        return auth.response(user);
    }
}
