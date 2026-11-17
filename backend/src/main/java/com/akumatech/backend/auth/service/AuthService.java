package com.akumatech.backend.auth.service;

import com.akumatech.backend.auth.dto.AuthResponse;
import com.akumatech.backend.auth.dto.LoginRequest;
import com.akumatech.backend.auth.dto.RegisterRequest;
import com.akumatech.backend.user.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository users, PasswordEncoder encoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.users = users;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (users.existsByCorreo(request.correo())) {
            throw new IllegalArgumentException("Este correo ya está registrado.");
        }
        if (users.existsByRut(request.rut())) {
            throw new IllegalArgumentException("Este RUT ya está registrado.");
        }
        User user = users.save(new User(request.nombre().trim(), request.correo().trim().toLowerCase(),
                encoder.encode(request.password()), request.rut().trim(), request.telefono().trim(), Role.USUARIO));
        return response(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo().trim().toLowerCase(), request.password()));
        return response(users.findByCorreo(request.correo().trim().toLowerCase()).orElseThrow());
    }

    public AuthResponse response(User user) {
        return new AuthResponse(jwtService.generate(user), user.getNombre(),
                user.getCorreo(), user.getRol().name());
    }
}
