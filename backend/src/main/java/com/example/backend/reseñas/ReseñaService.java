package com.example.backend.reseñas;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReseñaService {

  private final ReseñaRepository repo;

  public ReseñaService(ReseñaRepository repo) {
    this.repo = repo;
  }

  public List<ReseñaResponse> list() {
    return repo.findAll().stream().map(ReseñaResponse::from).toList();
  }

  public List<ReseñaResponse> listByTour(String tourId) {
    return repo.findByTourId(tourId).stream().map(ReseñaResponse::from).toList();
  }

  public ReseñaResponse get(String id) {
    return ReseñaResponse.from(repo.getOrThrow(id));
  }

  public ReseñaResponse create(ReseñaCreateRequest req) {
    ReseñaEntity e = new ReseñaEntity();
    e.setId(UUID.randomUUID().toString());
    e.setTourId(req.tourId());
    e.setClienteId(req.clienteId());
    e.setCalificacion(req.calificacion());
    e.setTitulo(req.titulo());
    e.setComentario(req.comentario());
    e.setVerificada(false);
    e.setFecha(Instant.now());
    return ReseñaResponse.from(repo.save(e));
  }

  public ReseñaResponse verificar(String id) {
    ReseñaEntity e = repo.getOrThrow(id);
    e.setVerificada(true);
    return ReseñaResponse.from(repo.save(e));
  }

  public void delete(String id) {
    if (!repo.existsById(id)) throw new NoSuchElementException("Reseña no encontrada");
    repo.deleteById(id);
  }
}
