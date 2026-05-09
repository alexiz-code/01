package com.example.backend.asientos;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/asientos")
public class AsientoController {

  private final AsientoService service;

  public AsientoController(AsientoService service) {
    this.service = service;
  }

  // GET /api/asientos?idAuto=xxx&estado=libre
  @GetMapping
  public List<AsientoResponse> list(
      @RequestParam(required = false) String idAuto,
      @RequestParam(required = false) String estado) {
    return service.list(idAuto, estado);
  }

  @GetMapping("/{id}")
  public AsientoResponse get(@PathVariable String id) {
    return service.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AsientoResponse create(@Valid @RequestBody AsientoCreateRequest req) {
    return service.create(req);
  }

  // PATCH /api/asientos/{id}/reservar  body: { "idReserva": "xxx" }
  @PatchMapping("/{id}/reservar")
  public AsientoResponse reservar(
      @PathVariable String id,
      @RequestBody java.util.Map<String, String> body) {
    return service.reservar(id, body.get("idReserva"));
  }

  // PATCH /api/asientos/{id}/liberar
  @PatchMapping("/{id}/liberar")
  public AsientoResponse liberar(@PathVariable String id) {
    return service.liberar(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    service.delete(id);
  }
}
