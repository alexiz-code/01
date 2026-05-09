import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe, SlicePipe, DecimalPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import {
  G7ApiService,
  UserDto, DestinoDto, TourDto, ReservaDto, PagoDto,
} from '../../core/g7-api.service';

type Section =  'overview' |
                'usuarios' |
                'destinos' | 
                'reservas' | 
                'tours' | 
                'pagos' | 
                'autos' | 
                'asientos';

interface NavItem { key: Section; label: string; icon: string; }

interface UserForm {
  nombre: string;
  email: string;
  telefono: string;
  rol: 'admin' | 'conductor';
  password: string;
}

interface AutoForm {
  placa: string;
  marca: string;
  modelo: string;
  color: string;
  anioFabrica: number;
  cantidadAsiento: number;
  tipo: string;
  conductor: string;
  estado: string;
}

interface TourForm {
  nombre: string;
  descripcion: string;
  destinoId: string;
  precioAdulto: number;
  precioNino: number;
  duracionDias: number;
  cuposTotal: number;
  dificultad: string;
  incluye: string;
  noIncluye: string;
  imagenUrl: string;
}

interface AutoDto {
  id: string;
  placa: string;
  marca: string;
  modelo: string;
  color: string;
  anioFabrica: number;
  cantidadAsiento: number;
  tipo: string;
  conductor: string;
  estado: string;
}

interface AsientoDto {
  id: string;
  idAuto: string;
  idReserva: string;
  numeroAsiento: string;
  estado: string;
}
@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe, SlicePipe, DecimalPipe],
  templateUrl: './users.html',
  styleUrls: ['./users.css'],
})
export class Users implements OnInit {

    private router = inject(Router);

  logout(): void {

    localStorage.clear();

    this.router.navigate(['/home']);
  }

  private readonly api = inject(G7ApiService);

  activeSection: Section = 'overview';
  sidebarCollapsed = false;
  busqueda = '';
  cargando = false;
  error: string | null = null;
  successMsg: string | null = null;

  usuarios:  UserDto[]    = [];
  destinos:  DestinoDto[] = [];
  tours:     TourDto[]    = [];
  reservas:  ReservaDto[] = [];
  pagos:     PagoDto[]    = [];
  autos:     AutoDto[]    = [];
  asientos:  AsientoDto[] = [];

  formUsuarioVisible  = false;
  formAutoVisible     = false;
  formTourVisible     = false;
  formAsientoVisible  = false;
  editingId: string | null = null;

  fUsuario: UserForm = this.emptyUsuario();
  fAuto:    AutoForm = this.emptyAuto();
  fTour:    TourForm = this.emptyTour();
  fAsiento: any      = this.emptyAsiento();

  navItems: NavItem[] = [
    { key: 'overview',  label: 'Resumen',   icon: '📊' },
    { key: 'usuarios',  label: 'Usuarios',  icon: '👥' },
    { key: 'destinos',  label: 'Destinos',  icon: '🗺'  },
    { key: 'tours',     label: 'Tours',     icon: '🧳' },
    { key: 'reservas',  label: 'Reservas',  icon: '📋' },
    { key: 'pagos',     label: 'Pagos',     icon: '💳' },
    { key: 'autos',     label: 'Vehículos', icon: '🚗' },
    { key: 'asientos',  label: 'Asientos',  icon: '💺' },
  ];

  get currentSectionLabel(): string {
    return this.navItems.find(n => n.key === this.activeSection)?.label ?? '';
  }

  ngOnInit(): void { this.cargarTodo(); }

  cargarTodo(): void {
    this.cargarUsuarios(); this.cargarDestinos(); this.cargarTours();
    this.cargarReservas(); this.cargarPagos(); this.cargarAutos(); this.cargarAsientos();
  }

  setSection(s: Section): void { this.activeSection = s; this.busqueda = ''; this.error = null; }
  recargar(): void { this.cargarTodo(); }

  private cargarUsuarios(): void { this.api.getUsers().subscribe({ next: d => this.usuarios = d, error: () => {} }); }
  private cargarDestinos(): void { this.api.getDestinos().subscribe({ next: d => this.destinos = d, error: () => {} }); }
  private cargarTours():    void { this.api.getTours().subscribe({ next: d => this.tours = d, error: () => {} }); }
  private cargarReservas(): void { this.api.getReservas().subscribe({ next: d => this.reservas = d, error: () => {} }); }
  private cargarPagos():    void { this.api.getPagos().subscribe({ next: d => this.pagos = d, error: () => {} }); }
  private cargarAutos():    void { (this.api as any).getAutos?.()?.subscribe?.({ next: (d: any) => this.autos = d, error: () => {} }); }
  private cargarAsientos(): void { (this.api as any).getAsientos?.()?.subscribe?.({ next: (d: any) => this.asientos = d, error: () => {} }); }


  abrirFormUsuario(): void {
    this.editingId = null; this.fUsuario = this.emptyUsuario();
    this.formUsuarioVisible = true; this.error = null;
  }
  abrirEditarUsuario(u: UserDto): void {
    this.editingId = u.id;
    this.fUsuario = { nombre: u.nombre, 
                      email: u.email, 
                      telefono: u.telefono, 
                      rol: u.rol as any, 
                      password: '' 
                    };

    this.formUsuarioVisible = true; this.error = null;
  }
  cerrarFormUsuario(): void { this.formUsuarioVisible = false; this.editingId = null; this.error = null; }

  guardarUsuario(): void {
    const { nombre, email, telefono, rol, password } = this.fUsuario;
    if (!nombre.trim() || !email.trim() || !telefono.trim()) { this.error = 'Nombre, email y teléfono son obligatorios.'; return; }
    if (!this.editingId && !password.trim())                  { this.error = 'La contraseña es obligatoria al crear.'; return; }
    if (password.trim() && password.length < 6)               { this.error = 'Contraseña mínimo 6 caracteres.'; return; }
    this.error = null;

    if (this.editingId) {
      const body: any = { nombre, email, telefono, rol };
      if (password.trim()) body.password = password;
      this.api.updateUser(this.editingId, body).subscribe({
        next: () => { this.cerrarFormUsuario(); this.cargarUsuarios(); this.flash('✓ Usuario actualizado'); },
        error: (e: any) => { this.error = e?.error?.message ?? 'Error al actualizar.'; },
      });
    } else {
      this.api.createUser({ nombre, email, telefono, rol, password }).subscribe({
        next: () => { this.cerrarFormUsuario(); this.cargarUsuarios(); this.flash('✓ Usuario creado'); },
        error: (e: any) => { this.error = e?.error?.message ?? 'Error al crear (¿email duplicado?).'; },
      });
    }
  }

  eliminarUsuario(u: UserDto): void {
    if (!confirm(`¿Eliminar a "${u.nombre}"?`)) return;
    this.api.deleteUser(u.id).subscribe({
      next: () => { this.cargarUsuarios(); this.flash('🗑 Usuario eliminado'); },
      error: () => { this.error = 'No se pudo eliminar.'; },
    });
  }

  usuariosFiltrados(): UserDto[] {
    const q = this.busqueda.toLowerCase();
    return q ? this.usuarios.filter(u =>
      u.nombre.toLowerCase().includes(q) || u.email.toLowerCase().includes(q)) : this.usuarios;
  }


  abrirFormAuto(): void { this.editingId = null; this.fAuto = this.emptyAuto(); this.formAutoVisible = true; this.error = null; }
  abrirEditarAuto(a: AutoDto): void {
    this.editingId = a.id;
    this.fAuto = {  placa: a.placa, 
                    marca: a.marca, 
                    modelo: a.modelo, 
                    color: a.color, 
                    anioFabrica: a.anioFabrica, 
                    cantidadAsiento: a.cantidadAsiento, 
                    tipo: a.tipo, 
                    conductor: a.conductor, 
                    estado: a.estado 
                  };

    this.formAutoVisible = true; this.error = null;
  }
  cerrarFormAuto(): void { this.formAutoVisible = false; this.editingId = null; this.error = null; }

  guardarAuto(): void {
    if (!this.fAuto.placa.trim() || !this.fAuto.marca.trim()) { this.error = 'Placa y marca son obligatorios.'; return; }
    this.error = null;
    const call = this.editingId
      ? (this.api as any).updateAuto?.(this.editingId, this.fAuto)
      : (this.api as any).createAuto?.(this.fAuto);
    call?.subscribe?.({
      next: () => { this.cerrarFormAuto(); this.cargarAutos(); this.flash('✓ Vehículo guardado'); },
      error: (e: any) => { this.error = e?.error?.message ?? 'Error al guardar vehículo.'; },
    });
  }

  eliminarAuto(a: AutoDto): void {
    if (!confirm(`¿Eliminar el vehículo ${a.placa}?`)) return;
    (this.api as any).deleteAuto?.(a.id)?.subscribe?.({
      next: () => { this.cargarAutos(); this.flash('🗑 Vehículo eliminado'); },
      error: () => { this.error = 'No se pudo eliminar.'; },
    });
  }

  autosFiltrados(): AutoDto[] {
    const q = this.busqueda.toLowerCase();
    return q ? this.autos.filter(a =>
      a.placa.toLowerCase().includes(q) ||
      a.marca.toLowerCase().includes(q) ||
      a.modelo.toLowerCase().includes(q)) : this.autos;
  }

  abrirFormTour(): void { this.editingId = null; this.fTour = this.emptyTour(); this.formTourVisible = true; this.error = null; }
  abrirEditarTour(t: TourDto): void {
    this.editingId = t.id;
    this.fTour = {  nombre: t.nombre, 
                    descripcion: t.descripcion, 
                    destinoId: t.destinoId, 
                    precioAdulto: t.precioAdulto, 
                    precioNino: t.precioNino, 
                    duracionDias: t.duracionDias, 
                    cuposTotal: t.cuposTotal, 
                    dificultad: t.dificultad, 
                    incluye: t.incluye, 
                    noIncluye: t.noIncluye, 
                    imagenUrl: t.imagenUrl 
                  };
    
    this.formTourVisible = true; this.error = null;
  }
  cerrarFormTour(): void { this.formTourVisible = false; this.editingId = null; this.error = null; }

  guardarTour(): void {
    if (!this.fTour.nombre.trim()) { this.error = 'El nombre es obligatorio.'; return; }
    this.error = null;
    const call = this.editingId
      ? this.api.updateTour(this.editingId, this.fTour)
      : this.api.createTour(this.fTour);
    call.subscribe({
      next: () => { this.cerrarFormTour(); this.cargarTours(); this.flash('✓ Tour guardado'); },
      error: (e: any) => { this.error = e?.error?.message ?? 'Error al guardar tour.'; },
    });
  }

  eliminarTour(t: TourDto): void {
    if (!confirm(`¿Eliminar el tour "${t.nombre}"?`)) return;
    this.api.deleteTour(t.id).subscribe({
      next: () => { this.cargarTours(); this.flash('🗑 Tour eliminado'); },
      error: () => { this.error = 'No se pudo eliminar.'; },
    });
  }

  toursFiltrados(): TourDto[] {
    const q = this.busqueda.toLowerCase();
    return q ? this.tours.filter(t => t.nombre.toLowerCase().includes(q)) : this.tours;
  }


  eliminarReserva(r: ReservaDto): void {
    if (!confirm(`¿Eliminar la reserva de ${r.nombre} ${r.apellido}?`)) return;
    this.api.deleteReserva(r.id).subscribe({
      next: () => { this.cargarReservas(); this.flash('🗑 Reserva eliminada'); },
      error: () => { this.error = 'No se pudo eliminar.'; },
    });
  }

  reservasFiltradas(): ReservaDto[] {
    const q = this.busqueda.toLowerCase();
    return q ? this.reservas.filter(r =>
      r.nombre.toLowerCase().includes(q) ||
      r.apellido.toLowerCase().includes(q) ||
      r.destino.toLowerCase().includes(q)) : this.reservas;
  }


  confirmarPago(p: PagoDto): void {
    this.api.confirmarPago(p.id).subscribe({
      next: () => { this.cargarPagos(); this.flash('✓ Pago confirmado'); },
      error: () => { this.error = 'No se pudo confirmar el pago.'; },
    });
  }

  abrirFormAsiento(): void { this.fAsiento = this.emptyAsiento(); this.formAsientoVisible = true; this.error = null; }
  cerrarFormAsiento(): void { this.formAsientoVisible = false; this.error = null; }

  guardarAsiento(): void {
    if (!this.fAsiento.idAuto || !this.fAsiento.numeroAsiento) { this.error = 'Vehículo y número son obligatorios.'; return; }
    this.error = null;
    (this.api as any).createAsiento?.({ idAuto: this.fAsiento.idAuto,
                                        numeroAsiento: this.fAsiento.numeroAsiento, 
                                        estado: this.fAsiento.estado 
                                      })
      ?.subscribe?.({
        next: () => { this.cerrarFormAsiento(); this.cargarAsientos(); this.flash('✓ Asiento creado'); },
        error: () => { this.error = 'No se pudo crear el asiento.'; },
      });
  }

  liberarAsiento(s: AsientoDto): void {
    (this.api as any).liberarAsiento?.(s.id)?.subscribe?.({
      next: () => { this.cargarAsientos(); this.flash('✓ Asiento liberado'); },
      error: () => { this.error = 'No se pudo liberar.'; },
    });
  }

  eliminarAsiento(s: AsientoDto): void {
    if (!confirm('¿Eliminar este asiento?')) return;
    (this.api as any).deleteAsiento?.(s.id)?.subscribe?.({
      next: () => { this.cargarAsientos(); this.flash('🗑 Asiento eliminado'); },
      error: () => { this.error = 'No se pudo eliminar.'; },
    });
  }

  asientosFiltrados(): AsientoDto[] {
    const q = this.busqueda.toLowerCase();
    return q ? this.asientos.filter(s => s.numeroAsiento.toLowerCase().includes(q)) : this.asientos;
  }

  totalAdmins():       number { return this.usuarios.filter(u => u.rol === 'admin').length; }
  totalConductores():  number { return this.usuarios.filter(u => u.rol === 'conductor').length; }
  totalPasajeros():    number { return this.reservas.reduce((s, r) => s + r.pasajeros, 0); }
  autosActivos():      number { return this.autos.filter(a => a.estado === 'activo').length; }
  totalMontoPagos():   number { return this.pagos.reduce((s, p) => s + p.monto, 0); }
  pagosCompletados():  number { return this.pagos.filter(p => p.estado === 'completado').length; }
  pagosPendientes():   number { return this.pagos.filter(p => p.estado === 'pendiente').length; }
  asientosLibres():    number { return this.asientos.filter(s => s.estado === 'libre').length; }
  asientosReservados():number { return this.asientos.filter(s => s.estado === 'reservado').length; }
  asientosOcupados():  number { return this.asientos.filter(s => s.estado === 'ocupado').length; }

  placaDeAuto(idAuto: string): string {
    return this.autos.find(a => a.id === idAuto)?.placa ?? idAuto?.slice(0, 8) ?? '—';
  }

  avatarColor(nombre: string): string {
    const p = ['#c0392b', '#2980b9', '#27ae60', '#8e44ad', '#d35400', '#16a085'];
    return p[nombre?.charCodeAt(0) % p.length] ?? '#888';
  }

  private flash(msg: string): void {
    this.successMsg = msg;
    setTimeout(() => (this.successMsg = null), 3000);
  }

  private emptyUsuario(): UserForm { return { nombre: '', 
                                              email: '', 
                                              telefono: '', 
                                              rol: 'conductor', 
                                              password: '' 
  }; 
  }

  private emptyAuto():    AutoForm { return { placa: '', 
                                              marca: '', 
                                              modelo: '', 
                                              color: '', 
                                              anioFabrica: 2020, 
                                              cantidadAsiento: 4, 
                                              tipo: 'minivan', 
                                              conductor: '', 
                                              estado: 'activo' 
  }; 
  }
  private emptyTour():    TourForm { return { nombre: '', 
                                              descripcion: '', 
                                              destinoId: '', 
                                              precioAdulto: 0, 
                                              precioNino: 0, 
                                              duracionDias: 1, 
                                              cuposTotal: 10, 
                                              dificultad: 'fácil', 
                                              incluye: '', 
                                              noIncluye: '', 
                                              imagenUrl: '' 
  };
  }

  private emptyAsiento(): any      { return { idAuto: '', 
                                              numeroAsiento: '', 
                                              estado: 'libre', 
                                              idReserva: '' 
  }; 
  }


}