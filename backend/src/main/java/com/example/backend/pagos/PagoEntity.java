package com.example.backend.pagos;

import java.time.Instant;

public class PagoEntity {

  private String id;
  private String reservaId;
  private Double monto;
  private String metodo;
  private String estado;
  private String referencia;
  private String comprobanteUrl;
  private Instant fechaPago;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }

  public String getReservaId() { return reservaId; }
  public void setReservaId(String reservaId) { this.reservaId = reservaId; }

  public Double getMonto() { return monto; }
  public void setMonto(Double monto) { this.monto = monto; }

  public String getMetodo() { return metodo; }
  public void setMetodo(String metodo) { this.metodo = metodo; }

  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }

  public String getReferencia() { return referencia; }
  public void setReferencia(String referencia) { this.referencia = referencia; }

  public String getComprobanteUrl() { return comprobanteUrl; }
  public void setComprobanteUrl(String comprobanteUrl) { this.comprobanteUrl = comprobanteUrl; }

  public Instant getFechaPago() { return fechaPago; }
  public void setFechaPago(Instant fechaPago) { this.fechaPago = fechaPago; }
}
