package com.akuma.tech.auth.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.akuma.tech.auth.dto.AuthResponse;
import com.akuma.tech.auth.dto.LoginRequest;
import com.akuma.tech.auth.dto.RegisterRequest;
import com.akuma.tech.auth.dto.UserResponse;
import com.akuma.tech.auth.model.User;
import com.akuma.tech.auth.repository.UserRepository;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndActiveTrue(request.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmailAndActiveTrue(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está registrado");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setUsername(request.email()); // Usamos el email como username para Postgres
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setActive(true); // Aseguramos que se cree activo si es necesario
        user.setAdmin(false);
        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(user.getEmail()));
    }


    //endpoint para eliminar usuario
    public void deleteUser(String email) {
        User user = userRepository.findByEmailAndActiveTrue(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        user.setActive(false); // Marcamos al usuario como inactivo
        userRepository.save(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isActive()
            ))
            .toList();
    }
}
