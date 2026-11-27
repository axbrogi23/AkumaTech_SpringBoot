package com.akuma.tech.auth.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import com.akuma.tech.auth.dto.AuthResponse;
import com.akuma.tech.auth.dto.LoginRequest;
import com.akuma.tech.auth.dto.UserResponse;
import com.akuma.tech.auth.dto.RegisterRequest;
import com.akuma.tech.auth.service.AuthService;

import java.util.List;

import jakarta.validation.Valid;


@RequestMapping("/api/auth") 
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
    //metodo para obtener todos los usuarios
    @GetMapping("/users")
    public List<UserResponse> gettAllUsers()
        return authservice.getAllUsers();
    }
}
