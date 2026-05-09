package com.example.backend.pagos;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

  private final PagoService service;

  public PagoController(PagoService service) {
    this.service = service;
  }

  @GetMapping
  public List<PagoResponse> list(@RequestParam(required = false) String reservaId) {
    if (reservaId != null) return service.listByReserva(reservaId);
    return service.list();
  }

  @GetMapping("/{id}")
  public PagoResponse get(@PathVariable String id) {
    return service.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PagoResponse create(@Valid @RequestBody PagoCreateRequest req) {
    return service.create(req);
  }

  @PatchMapping("/{id}/confirmar")
  public PagoResponse confirmar(@PathVariable String id) {
    return service.confirmar(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id) {
    service.delete(id);
  }
}
