package com.akumatech.backend.config;

import com.akumatech.backend.user.Role;
import com.akumatech.backend.user.User;
import com.akumatech.backend.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean CommandLineRunner seedAdmin(UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (!users.existsByCorreo("admin@gmail.com")) {
                users.save(new User("Administrador", "admin@gmail.com", encoder.encode("admin123"),
                        "11.111.111-1", "+56900000000", Role.ADMIN));
            }
        };
    }
}
