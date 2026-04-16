package com.example.backend.destinos;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/destinos")
public class DestinoController {

  private final DestinoRepository repo;

  public DestinoController(DestinoRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public List<DestinoResponse> list() {
    return repo.findAllOrderByTitle().stream().map(DestinoResponse::from).toList();
  }
}

