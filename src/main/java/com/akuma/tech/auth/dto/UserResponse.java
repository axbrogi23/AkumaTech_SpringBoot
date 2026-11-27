package com.akuma.tech.auth.dto;

public record UserResponse(Long id, String username, String email, boolean active) {
}
