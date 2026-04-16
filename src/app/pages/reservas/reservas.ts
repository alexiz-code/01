import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { G7ApiService, ReservaCreatePayload, DestinoDto } from '../../core/g7-api.service';

interface DestinoOption {
  valor: string;
  nombre: string;
}

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reservas.html',
  styleUrl: './reservas.css',
})
export class Reservas implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(G7ApiService);

  formulario: FormGroup;
  enviado = false;
  cargando = false;
  errorApi: string | null = null;

  destinos: DestinoOption[] = [];

  private readonly destinosFallback: DestinoOption[] = [
    { valor: 'pampa de quinua', nombre: 'Pampa de Quinua' },
    { valor: 'machu picchu', nombre: 'Machu Picchu' },
    { valor: 'cusco', nombre: 'Cusco' },
    { valor: 'lago titicaca', nombre: 'Lago Titicaca' },
    { valor: 'sarhua', nombre: 'Sarhua' },
  ];

  constructor() {
    this.formulario = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      apellido: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', [Validators.required, Validators.pattern(/^\d{9,15}$/)]],
      destino: ['', Validators.required],
      fechaIda: ['', Validators.required],
      fechaVuelta: ['', Validators.required],
      pasajeros: [1, [Validators.required, Validators.min(1), Validators.max(20)]],
      clase: ['economica', Validators.required],
      notas: [''],
    });
  }

  ngOnInit(): void {
    this.api.getDestinos().subscribe({
      next: (list: DestinoDto[]) => {
        this.destinos = list.map((d) => ({
          valor: (d.title ?? d.name ?? '').toLowerCase().trim(),
          nombre: d.title || d.name,
        }));
        if (this.destinos.length === 0) {
          this.destinos = [...this.destinosFallback];
        }
      },
      error: () => {
        this.destinos = [...this.destinosFallback];
      },
    });
  }

  campo(nombre: string) {
    return this.formulario.get(nombre);
  }

  invalido(nombre: string): boolean {
    const c = this.campo(nombre);
    return !!(c && c.invalid && (c.dirty || c.touched));
  }

  onSubmit(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    this.cargando = true;
    this.errorApi = null;

    const v = this.formulario.getRawValue();
    const payload: ReservaCreatePayload = {
      nombre: v.nombre,
      apellido: v.apellido,
      email: v.email,
      telefono: String(v.telefono),
      destino: v.destino,
      fechaIda: v.fechaIda,
      fechaVuelta: v.fechaVuelta,
      pasajeros: Number(v.pasajeros),
      clase: v.clase,
      notas: v.notas ?? '',
    };

    this.api.createReserva(payload).subscribe({
      next: () => {
        this.cargando = false;
        this.enviado = true;
      },
      error: () => {
        this.cargando = false;
        this.errorApi = 'No se pudo enviar la reserva. Verifica que el backend esté en marcha.';
      },
    });
  }

  nuevaReserva(): void {
    this.enviado = false;
    this.errorApi = null;
    this.formulario.reset({ pasajeros: 1, clase: 'economica' });
  }
}
