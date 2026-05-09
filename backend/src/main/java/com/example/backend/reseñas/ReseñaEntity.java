package com.example.backend.reseñas;

import java.time.Instant;

public class ReseñaEntity {

  private String id;
  private String tourId;
  private String clienteId;
  private int calificacion;   // 1-5
  private String titulo;
  private String comentario;
  private boolean verificada;
  private Instant fecha;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getTourId() { return tourId; }
  public void setTourId(String tourId) { this.tourId = tourId; }

  public String getClienteId() { return clienteId; }
  public void setClienteId(String clienteId) { this.clienteId = clienteId; }

  public int getCalificacion() { return calificacion; }
  public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

  public String getTitulo() { return titulo; }
  public void setTitulo(String titulo) { this.titulo = titulo; }

  public String getComentario() { return comentario; }
  public void setComentario(String comentario) { this.comentario = comentario; }

  public boolean isVerificada() { return verificada; }
  public void setVerificada(boolean verificada) { this.verificada = verificada; }

  public Instant getFecha() { return fecha; }
  public void setFecha(Instant fecha) { this.fecha = fecha; }
}
