import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { G7ApiService, UserDto, UserCreatePayload } from '../../core/g7-api.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './users.html',
  styleUrls: ['./users.css'],
})
export class Users implements OnInit {
  private readonly api = inject(G7ApiService);

  formVisible = false;
  busqueda = '';

  nuevoUsuario: UserCreatePayload = {
    nombre: '',
    email: '',
    telefono: '',
    rol: 'cliente',
    reservas: 0,
  };

  /** Id del usuario en edición; null = alta */
  editingId: string | null = null;

  usuarios: UserDto[] = [];

  cargando = false;
  error: string | null = null;

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  cargarUsuarios(): void {
    this.cargando = true;
    this.error = null;
    this.api.getUsers().subscribe({
      next: (list) => {
        this.usuarios = list;
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se pudo conectar con el servidor. ¿Está el backend en ejecución?';
        this.cargando = false;
        this.usuarios = [];
      },
    });
  }

  toggleForm(): void {
    this.formVisible = !this.formVisible;
    if (!this.formVisible) {
      this.editingId = null;
      this.nuevoUsuario = { nombre: '', email: '', telefono: '', rol: 'cliente', reservas: 0 };
    }
  }

  abrirNuevo(): void {
    this.editingId = null;
    this.nuevoUsuario = { nombre: '', email: '', telefono: '', rol: 'cliente', reservas: 0 };
    this.formVisible = true;
  }

  abrirEditar(u: UserDto): void {
    this.editingId = u.id;
    this.nuevoUsuario = {
      nombre: u.nombre,
      email: u.email,
      telefono: u.telefono,
      rol: u.rol,
      reservas: u.reservas,
    };
    this.formVisible = true;
  }

  guardarUsuario(): void {
    if (!this.nuevoUsuario.nombre?.trim() || !this.nuevoUsuario.email?.trim()) {
      return;
    }

    const body: UserCreatePayload = { ...this.nuevoUsuario };

    if (this.editingId) {
      this.api.updateUser(this.editingId, body).subscribe({
        next: () => {
          this.toggleForm();
          this.cargarUsuarios();
        },
        error: () => {
          this.error = 'No se pudo actualizar el usuario.';
        },
      });
    } else {
      this.api.createUser(body).subscribe({
        next: () => {
          this.toggleForm();
          this.cargarUsuarios();
        },
        error: () => {
          this.error = 'No se pudo crear el usuario (¿email duplicado?).';
        },
      });
    }
  }

  eliminarUsuario(u: UserDto): void {
    if (!confirm(`¿Eliminar a ${u.nombre}?`)) return;
    this.api.deleteUser(u.id).subscribe({
      next: () => this.cargarUsuarios(),
      error: () => (this.error = 'No se pudo eliminar el usuario.'),
    });
  }

  usuariosFiltrados(): UserDto[] {
    const q = this.busqueda.toLowerCase();
    return this.usuarios.filter(
      (u) => u.nombre.toLowerCase().includes(q) || u.email.toLowerCase().includes(q),
    );
  }

  totalAdmins(): number {
    return this.usuarios.filter((u) => u.rol === 'admin').length;
  }

  totalClientes(): number {
    return this.usuarios.filter((u) => u.rol === 'cliente').length;
  }

  totalReservas(): number {
    return this.usuarios.reduce((s, u) => s + u.reservas, 0);
  }

  trackById(_index: number, u: UserDto): string {
    return u.id;
  }
}
