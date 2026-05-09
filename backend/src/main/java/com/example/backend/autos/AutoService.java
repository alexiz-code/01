package com.example.backend.autos;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AutoService {

  private final AutoRepository repo;

  public AutoService(AutoRepository repo) {
    this.repo = repo;
  }

  public List<AutoResponse> list() {
    return repo.findAll().stream().map(AutoResponse::from).toList();
  }

  public List<AutoResponse> listByEstado(String estado) {
    return repo.findByEstado(estado).stream().map(AutoResponse::from).toList();
  }

  public AutoResponse get(String id) {
    return AutoResponse.from(repo.getOrThrow(id));
  }

  public AutoResponse create(AutoCreateRequest req) {
    if (repo.existsByPlaca(req.placa())) {
      throw new IllegalArgumentException("Ya existe un auto con esa placa: " + req.placa());
    }
    AutoEntity e = new AutoEntity();
    e.setId(UUID.randomUUID().toString());
    e.setPlaca(req.placa().toUpperCase());
    e.setMarca(req.marca());
    e.setModelo(req.modelo());
    e.setColor(req.color());
    e.setAnioFabrica(req.anioFabrica());
    e.setCantidadAsiento(req.cantidadAsiento());
    e.setTipo(req.tipo());
    e.setConductor(req.conductor());
    e.setEstado(req.estado());
    return AutoResponse.from(repo.save(e));
  }

  public AutoResponse update(String id, AutoCreateRequest req) {
    AutoEntity e = repo.getOrThrow(id);
    if (!e.getPlaca().equalsIgnoreCase(req.placa()) && repo.existsByPlaca(req.placa())) {
      throw new IllegalArgumentException("Ya existe un auto con esa placa: " + req.placa());
    }
    e.setPlaca(req.placa().toUpperCase());
    e.setMarca(req.marca());
    e.setModelo(req.modelo());
    e.setColor(req.color());
    e.setAnioFabrica(req.anioFabrica());
    e.setCantidadAsiento(req.cantidadAsiento());
    e.setTipo(req.tipo());
    e.setConductor(req.conductor());
    e.setEstado(req.estado());
    return AutoResponse.from(repo.save(e));
  }

  public void delete(String id) {
    if (!repo.existsById(id)) throw new NoSuchElementException("Auto no encontrado");
    repo.deleteById(id);
  }
}
