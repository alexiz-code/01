package com.example.backend.asientos;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AsientoService {

  private final AsientoRepository repo;

  public AsientoService(AsientoRepository repo) {
    this.repo = repo;
  }

  public List<AsientoResponse> list(String idAuto, String estado) {
    if (idAuto != null && estado != null)
      return repo.findByIdAutoAndEstado(idAuto, estado).stream().map(AsientoResponse::from).toList();
    if (idAuto != null)
      return repo.findByIdAuto(idAuto).stream().map(AsientoResponse::from).toList();
    if (estado != null)
      return repo.findByEstado(estado).stream().map(AsientoResponse::from).toList();
    return repo.findAll().stream().map(AsientoResponse::from).toList();
  }

  public AsientoResponse get(String id) {
    return AsientoResponse.from(repo.getOrThrow(id));
  }

  public AsientoResponse create(AsientoCreateRequest req) {
    AsientoEntity e = new AsientoEntity();
    e.setId(UUID.randomUUID().toString());
    e.setIdAuto(req.idAuto());
    e.setIdReserva(req.idReserva());
    e.setNumeroAsiento(req.numeroAsiento());
    e.setEstado(req.estado() != null ? req.estado() : "libre");
    return AsientoResponse.from(repo.save(e));
  }

  // Asignar un asiento a una reserva
  public AsientoResponse reservar(String id, String idReserva) {
    AsientoEntity e = repo.getOrThrow(id);
    if ("ocupado".equals(e.getEstado()))
      throw new IllegalStateException("El asiento ya está ocupado");
    e.setIdReserva(idReserva);
    e.setEstado("reservado");
    return AsientoResponse.from(repo.save(e));
  }

  // Liberar un asiento
  public AsientoResponse liberar(String id) {
    AsientoEntity e = repo.getOrThrow(id);
    e.setIdReserva(null);
    e.setEstado("libre");
    return AsientoResponse.from(repo.save(e));
  }

  public void delete(String id) {
    if (!repo.existsById(id)) throw new NoSuchElementException("Asiento no encontrado");
    repo.deleteById(id);
  }
}
