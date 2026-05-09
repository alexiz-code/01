package com.example.backend.pagos;

import java.time.Instant;

public record PagoResponse(
    String id,
    String reservaId,
    double monto,
    String metodo,
    String estado,
    String referencia,
    String comprobanteUrl,
    Instant fechaPago) {

  public static PagoResponse from(PagoEntity e) {
    return new PagoResponse(
        e.getId(),
        e.getReservaId(),
        e.getMonto() != null ? e.getMonto() : 0,
        e.getMetodo(),
        e.getEstado(),
        e.getReferencia(),
        e.getComprobanteUrl(),
        e.getFechaPago());
  }
}
