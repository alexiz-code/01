package com.example.backend.pagos;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PagoService {

  private final PagoRepository repo;

  public PagoService(PagoRepository repo) {
    this.repo = repo;
  }

  public List<PagoResponse> list() {
    return repo.findAll().stream().map(PagoResponse::from).toList();
  }

  public List<PagoResponse> listByReserva(String reservaId) {
    return repo.findByReservaId(reservaId).stream().map(PagoResponse::from).toList();
  }

  public PagoResponse get(String id) {
    return PagoResponse.from(repo.getOrThrow(id));
  }

  public PagoResponse create(PagoCreateRequest req) {
    PagoEntity e = new PagoEntity();
    e.setId(UUID.randomUUID().toString());
    e.setReservaId(req.reservaId());
    e.setMonto(req.monto());
    e.setMetodo(req.metodo());
    e.setEstado("pendiente");
    e.setReferencia(req.referencia());
    e.setComprobanteUrl(req.comprobanteUrl());
    e.setFechaPago(Instant.now());
    return PagoResponse.from(repo.save(e));
  }

  public PagoResponse confirmar(String id) {
    PagoEntity e = repo.getOrThrow(id);
    e.setEstado("completado");
    return PagoResponse.from(repo.save(e));
  }

  public void delete(String id) {
    if (!repo.existsById(id)) throw new NoSuchElementException("Pago no encontrado");
    repo.deleteById(id);
  }
}
