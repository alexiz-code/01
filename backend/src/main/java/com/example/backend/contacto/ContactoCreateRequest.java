package com.example.backend.contacto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactoCreateRequest(
    @NotBlank String nombre, @NotBlank @Email String email, @NotBlank String mensaje) {}

