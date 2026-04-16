package com.example.backend.reservas;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.time.LocalDate;

public class ReservaEntity {

  private String id;

  @NotBlank
  private String nombre;

  @NotBlank
  private String apellido;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String telefono;

  @NotBlank
  private String destino;

  private LocalDate fechaIda;

  private LocalDate fechaVuelta;

  @Min(1)
  @Max(20)
  private int pasajeros;

  @NotBlank
  private String clase;

  private String notas;

  private Instant createdAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getDestino() {
    return destino;
  }

  public void setDestino(String destino) {
    this.destino = destino;
  }

  public LocalDate getFechaIda() {
    return fechaIda;
  }

  public void setFechaIda(LocalDate fechaIda) {
    this.fechaIda = fechaIda;
  }

  public LocalDate getFechaVuelta() {
    return fechaVuelta;
  }

  public void setFechaVuelta(LocalDate fechaVuelta) {
    this.fechaVuelta = fechaVuelta;
  }

  public int getPasajeros() {
    return pasajeros;
  }

  public void setPasajeros(int pasajeros) {
    this.pasajeros = pasajeros;
  }

  public String getClase() {
    return clase;
  }

  public void setClase(String clase) {
    this.clase = clase;
  }

  public String getNotas() {
    return notas;
  }

  public void setNotas(String notas) {
    this.notas = notas;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}

