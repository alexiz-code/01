package com.example.backend.tours;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tours")
public class TourController {

  private final TourService service;

  public TourController(TourService service) {
    this.service = service;
  }

  @GetMapping
  public List<TourResponse> list(@RequestParam(required = false) String destinoId) {
    if (destinoId != null) return service.listByDestino(destinoId);
    return service.list();
  }

  @GetMapping("/{id}")
  public TourResponse get(@PathVariable String id) {
    return service.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TourResponse create(@Valid @RequestBody TourCreateRequest req) {
    return service.create(req);
  }

  @PutMapping("/{id}")
  public TourResponse update(@PathVariable String id, @Valid @RequestBody TourCreateRequest req) {
    return service.update(id, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    service.delete(id);
  }
}
