package com.example.backend.seed;

import com.example.backend.destinos.DestinoEntity;
import com.example.backend.destinos.DestinoRepository;
import com.example.backend.users.UserEntity;
import com.example.backend.users.UserRepository;
import com.example.backend.users.UserRole;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepo;
  private final DestinoRepository destinoRepo;

  public DataInitializer(UserRepository userRepo, DestinoRepository destinoRepo) {
    this.userRepo = userRepo;
    this.destinoRepo = destinoRepo;
  }

  @Override
  public void run(String... args) {
    seedUsers();
    seedDestinos();
  }

  private void seedUsers() {
    if (userRepo.count() > 0) return;

    for (UserEntity u :
        List.of(
            user("Carlos Quispe", "carlos@gmail.com", "+51 987 654 321", UserRole.admin, 12),
            user("María Huamán", "maria@gmail.com", "+51 976 543 210", UserRole.cliente, 5),
            user("Luis Flores", "luis@gmail.com", "+51 965 432 109", UserRole.cliente, 3),
            user("Ana Torres", "ana@gmail.com", "+51 954 321 098", UserRole.cliente, 8),
            user("Pedro Mendoza", "pedro@gmail.com", "+51 943 210 987", UserRole.admin, 20))) {
      userRepo.save(u);
    }
  }

  private UserEntity user(String nombre, String email, String telefono, UserRole rol, int reservas) {
    UserEntity e = new UserEntity();
    e.setId(UUID.randomUUID().toString());
    e.setNombre(nombre);
    e.setEmail(email);
    e.setTelefono(telefono);
    e.setRol(rol);
    e.setReservas(reservas);
    return e;
  }

  private void seedDestinos() {
    if (destinoRepo.count() > 0) return;

    for (DestinoEntity d :
        List.of(
            destino(
                "histórica",
                "Pampa de Quinua",
                "Ubicada cerca de Ayacucho, paisaje andino de gran valor histórico y natural.",
                "Historia Viva",
                "https://www.ytuqueplanes.com/imagenes/fotos/novedades/sierra-pampa-quinua.JPG",
                "https://www.ytuqueplanes.com/imagenes/fotos/novedades/sierra-pampa-quinua.JPG"),
            destino(
                "Mística",
                "Machu Picchu",
                "Antigua ciudad inca rodeada de montañas. Una de las maravillas del mundo.",
                "Maravilla",
                "https://images.pexels.com/photos/259967/pexels-photo-259967.jpeg",
                "https://images.pexels.com/photos/259967/pexels-photo-259967.jpeg"),
            destino(
                "Histórico",
                "Cusco",
                "Ciudad andina llena de historia, antigua capital del Imperio Inca.",
                "Milenario",
                "https://images.pexels.com/photos/21014/pexels-photo.jpg",
                "https://images.pexels.com/photos/21014/pexels-photo.jpg"),
            destino(
                "Místico",
                "Lago Titicaca",
                "El lago navegable más alto del mundo, rodeado de tradiciones ancestrales.",
                "Sagrado",
                "https://images.pexels.com/photos/753626/pexels-photo-753626.jpeg",
                "https://images.pexels.com/photos/753626/pexels-photo-753626.jpeg"),
            destino(
                "Tradicional",
                "Sarhua",
                "Pueblo andino conocido por su arte tradicional y las tablas de Sarhua.",
                "Cultural",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4qHUBk0VGAdXrmJiwT98ksH9f9KWWm7Sicw&s",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4qHUBk0VGAdXrmJiwT98ksH9f9KWWm7Sicw&s"))) {
      destinoRepo.save(d);
    }
  }

  private DestinoEntity destino(
      String label, String title, String desc, String name, String bg, String thumb) {
    DestinoEntity d = new DestinoEntity();
    d.setId(UUID.randomUUID().toString());
    d.setLabel(label);
    d.setTitle(title);
    d.setDesc(desc);
    d.setName(name);
    d.setBg(bg);
    d.setThumb(thumb);
    return d;
  }
}

