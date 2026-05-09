package com.example.backend.autos;

public record AutoResponse(
  String id,
  String placa,
  String marca,
  String modelo,
  String color,
  int anioFabrica,
  int cantidadAsiento,
  String tipo,
  String conductor,
  String estado) {

  public static AutoResponse from(AutoEntity e) {
    return new AutoResponse(
      e.getId(),
      e.getPlaca(),
      e.getMarca(),
      e.getModelo(),
      e.getColor(),
      e.getAnioFabrica() != null ? e.getAnioFabrica() : 0,
      e.getCantidadAsiento() != null ? e.getCantidadAsiento() : 0,
      e.getTipo(),
      e.getConductor(),
      e.getEstado());
  }
}
