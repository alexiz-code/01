package com.example.backend.reseñas;

import java.time.Instant;

public record ReseñaResponse(
    String id,
    String tourId,
    String clienteId,
    int calificacion,
    String titulo,
    String comentario,
    boolean verificada,
    Instant fecha) {

  public static ReseñaResponse from(ReseñaEntity e) {
    return new ReseñaResponse(
        e.getId(), e.getTourId(), e.getClienteId(),
        e.getCalificacion(), e.getTitulo(), e.getComentario(),
        e.isVerificada(), e.getFecha());
  }
}
