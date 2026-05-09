package com.example.backend.autos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AutoCreateRequest(
  @NotBlank String placa,
  @NotBlank String marca,
  @NotBlank String modelo,
  @NotBlank String color,
  @NotNull @Min(1990) Integer anioFabrica,
  @NotNull @Min(1) Integer cantidadAsiento,
  @NotBlank String tipo,
  String conductor,
  @NotBlank String estado) {}
