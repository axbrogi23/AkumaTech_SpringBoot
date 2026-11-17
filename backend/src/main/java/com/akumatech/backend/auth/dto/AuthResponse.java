package com.akumatech.backend.auth.dto;



public record AuthResponse(String token, String nombre, String correo, String rol) {
}
