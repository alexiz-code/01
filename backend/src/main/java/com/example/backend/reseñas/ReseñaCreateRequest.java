package com.example.backend.reseñas;

import jakarta.validation.constraints.*;

public record ReseñaCreateRequest(
    @NotBlank String tourId,
    @NotBlank String clienteId,
    @Min(1) @Max(5) int calificacion,
    @NotBlank String titulo,
    @NotBlank String comentario) {}
