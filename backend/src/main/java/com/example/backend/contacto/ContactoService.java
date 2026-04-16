package com.example.backend.contacto;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ContactoService {

  private final ContactoRepository repo;

  public ContactoService(ContactoRepository repo) {
    this.repo = repo;
  }

  public List<ContactoResponse> list() {
    return repo.findAllOrderByCreatedAtDesc().stream().map(ContactoResponse::from).toList();
  }

  public ContactoResponse get(String id) {
    return ContactoResponse.from(
        repo.findById(id).orElseThrow(() -> new NoSuchElementException("Mensaje no encontrado")));
  }

  public ContactoResponse create(ContactoCreateRequest req) {
    ContactoMessageEntity e = new ContactoMessageEntity();
    e.setId(UUID.randomUUID().toString());
    e.setNombre(req.nombre());
    e.setEmail(req.email());
    e.setMensaje(req.mensaje());
    e.setCreatedAt(Instant.now());
    return ContactoResponse.from(repo.save(e));
  }

  public void delete(String id) {
    if (!repo.existsById(id)) throw new NoSuchElementException("Mensaje no encontrado");
    repo.deleteById(id);
  }
}

