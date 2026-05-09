package com.example.backend.pagos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PagoCreateRequest(
    @NotBlank String reservaId,
    @NotNull @Positive Double monto,
    @NotBlank String metodo,
    String referencia,
    String comprobanteUrl) {}
