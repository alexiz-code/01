package com.example.backend.autos;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autos")
public class AutoController {

  private final AutoService service;

  public AutoController(AutoService service) {
    this.service = service;
  }

  @GetMapping
  public List<AutoResponse> list(@RequestParam(required = false) String estado) {
    if (estado != null) return service.listByEstado(estado);
    return service.list();
  }

  @GetMapping("/{id}")
  public AutoResponse get(@PathVariable String id) {
    return service.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AutoResponse create(@Valid @RequestBody AutoCreateRequest req) {
    return service.create(req);
  }

  @PutMapping("/{id}")
  public AutoResponse update(@PathVariable String id, @Valid @RequestBody AutoCreateRequest req) {
    return service.update(id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    service.delete(id);
  }
}
