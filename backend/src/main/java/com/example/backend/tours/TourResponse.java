package com.example.backend.tours;

import java.time.Instant;

public record TourResponse(
    String id,
    String nombre,
    String descripcion,
    String destinoId,
    double precioAdulto,
    double precioNino,
    int duracionDias,
    int cuposTotal,
    int cuposDisponibles,
    String dificultad,
    String incluye,
    String noIncluye,
    String imagenUrl,
    boolean activo,
    Instant creadoEn) {

  public static TourResponse from(TourEntity e) {
    return new TourResponse(
        e.getId(),
        e.getNombre(),
        e.getDescripcion(),
        e.getDestinoId(),
        e.getPrecioAdulto() != null ? e.getPrecioAdulto() : 0,
        e.getPrecioNino() != null ? e.getPrecioNino() : 0,
        e.getDuracionDias(),
        e.getCuposTotal(),
        e.getCuposDisponibles(),
        e.getDificultad(),
        e.getIncluye(),
        e.getNoIncluye(),
        e.getImagenUrl(),
        e.isActivo(),
        e.getCreadoEn());
  }
}
