package com.example.backend.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
    @NotBlank String nombre,
    @NotBlank @Email String email,
    @NotBlank String telefono,
    @NotNull UserRole rol,
    @Min(0) int reservas) {}

