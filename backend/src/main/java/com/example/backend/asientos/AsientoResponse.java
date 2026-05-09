package com.example.backend.asientos;

public record AsientoResponse(
    String id,
    String idAuto,
    String idReserva,
    String numeroAsiento,
    String estado) {

  public static AsientoResponse from(AsientoEntity e) {
    return new AsientoResponse(
        e.getId(),
        e.getIdAuto(),
        e.getIdReserva(),
        e.getNumeroAsiento(),
        e.getEstado());
  }
}
