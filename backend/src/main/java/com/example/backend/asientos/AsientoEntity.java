package com.example.backend.asientos;

public class AsientoEntity {

  private String id;
  private String idAuto;       // referencia al auto
  private String idReserva;    // referencia a la reserva (puede ser null si libre)
  private String numeroAsiento; // ej: "A1", "B3"
  private String estado;       // "libre" | "ocupado" | "reservado"

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getIdAuto() { return idAuto; }
  public void setIdAuto(String idAuto) { this.idAuto = idAuto; }

  public String getIdReserva() { return idReserva; }
  public void setIdReserva(String idReserva) { this.idReserva = idReserva; }

  public String getNumeroAsiento() { return numeroAsiento; }
  public void setNumeroAsiento(String numeroAsiento) { this.numeroAsiento = numeroAsiento; }

  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
}
