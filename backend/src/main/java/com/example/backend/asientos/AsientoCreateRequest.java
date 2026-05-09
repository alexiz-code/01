package com.example.backend.asientos;

import jakarta.validation.constraints.NotBlank;

public record AsientoCreateRequest(
    @NotBlank String idAuto,
    String idReserva,
    @NotBlank String numeroAsiento,
    @NotBlank String estado) {}
