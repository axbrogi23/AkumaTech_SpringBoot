package com.akuma.tech.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akuma.tech.auth.dto.AuthResponse;
import com.akuma.tech.auth.dto.LoginRequest;
import com.akuma.tech.auth.dto.RegisterRequest;
import com.akuma.tech.auth.service.AuthService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;


@RequestMapping("/api/v1/auth") 
@RestController
public class AuthController {

    private final AuthService authService;


    // Constructor injection for AuthService
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    // Endpoint para login
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }


    //Endpoint para registro de usuarios
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }
    
    //endpoint para eliminar usuario
    @DeleteMapping("/delete/{email}")
    public void deleteUser(@PathVariable String email) {
        authService.deleteUser(email);
    }
}
