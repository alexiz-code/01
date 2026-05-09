package com.example.backend.reseñas;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resenas")
public class ReseñaController {

  private final ReseñaService service;

  public ReseñaController(ReseñaService service) {
    this.service = service;
  }

  @GetMapping
  public List<ReseñaResponse> list(@RequestParam(required = false) String tourId) {
    if (tourId != null) return service.listByTour(tourId);
    return service.list();
  }

  @GetMapping("/{id}")
  public ReseñaResponse get(@PathVariable String id) {
    return service.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ReseñaResponse create(@Valid @RequestBody ReseñaCreateRequest req) {
    return service.create(req);
  }

  @PatchMapping("/{id}/verificar")
  public ReseñaResponse verificar(@PathVariable String id) {
    return service.verificar(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    service.delete(id);
  }
}
