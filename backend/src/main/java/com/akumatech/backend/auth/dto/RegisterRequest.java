package com.akumatech.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest (
        @NotBlank String nombre,
        @Email @NotBlank String correo,
        @NotBlank String password,
        @NotBlank String rut,
        @NotBlank String telefono) {
}
