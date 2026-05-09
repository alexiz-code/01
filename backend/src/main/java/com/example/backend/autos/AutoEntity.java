package com.example.backend.autos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AutoEntity {

  private String id;

  @NotBlank
  private String placa;

  @NotBlank
  private String marca;

  @NotBlank
  private String modelo;

  @NotBlank
  private String color;

  @NotNull
  private Integer anioFabrica;

  @NotNull
  private Integer cantidadAsiento;

  @NotBlank
  private String tipo;       // "minivan" | "bus" | "sedan" | "suv"

  private String conductor;  // nombre del conductor asignado

  @NotBlank
  private String estado;     // "activo" | "mantenimiento" | "inactivo"

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getPlaca() { return placa; }
  public void setPlaca(String placa) { this.placa = placa; }

  public String getMarca() { return marca; }
  public void setMarca(String marca) { this.marca = marca; }

  public String getModelo() { return modelo; }
  public void setModelo(String modelo) { this.modelo = modelo; }

  public String getColor() { return color; }
  public void setColor(String color) { this.color = color; }

  public Integer getAnioFabrica() { return anioFabrica; }
  public void setAnioFabrica(Integer anioFabrica) { this.anioFabrica = anioFabrica; }

  public Integer getCantidadAsiento() { return cantidadAsiento; }
  public void setCantidadAsiento(Integer cantidadAsiento) { this.cantidadAsiento = cantidadAsiento; }

  public String getTipo() { return tipo; }
  public void setTipo(String tipo) { this.tipo = tipo; }

  public String getConductor() { return conductor; }
  public void setConductor(String conductor) { this.conductor = conductor; }

  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
}
