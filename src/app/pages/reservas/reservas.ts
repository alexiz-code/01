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

  // 🚍 ASIENTOS
  asientos: any[][] = [];

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
      },
      error: () => {
        this.destinos = [
          { valor: 'machu picchu', nombre: 'Machu Picchu' },
          { valor: 'cusco', nombre: 'Cusco' },
          { valor: 'titicaca', nombre: 'Lago Titicaca' },
        ];
      },
    });

    this.generarAsientos();
  }

  // 🚍 ASIENTOS
  generarAsientos() {
    let contador = 1;

    this.asientos = Array.from({ length: 5 }, () =>
      Array.from({ length: 4 }, () => {
        const ocupado = Math.random() < 0.3;

        return {
          numero: contador++,
          ocupado,
          seleccionado: false,
        };
      })
    );
  }

  seleccionarAsiento(i: number, j: number) {
    const asiento = this.asientos[i][j];

    if (asiento.ocupado) return;

    const seleccionados = this.asientos.flat().filter(a => a.seleccionado);

    if (!asiento.seleccionado && seleccionados.length >= this.formulario.value.pasajeros) {
      return;
    }

    asiento.seleccionado = !asiento.seleccionado;
  }

  // ✔ FIX TRACKBY
  trackByValor(index: number, item: any): string {
    return item.valor;
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

    const v = this.formulario.value;

    const payload: ReservaCreatePayload = {
      nombre: v.nombre,
      apellido: v.apellido,
      email: v.email,
      telefono: v.telefono,
      destino: v.destino,
      fechaIda: v.fechaIda,
      fechaVuelta: v.fechaVuelta,
      pasajeros: v.pasajeros,
      clase: v.clase,
      notas: v.notas,
    };

    this.api.createReserva(payload).subscribe({
      next: () => {
        this.cargando = false;
        this.enviado = true;
      },
      error: () => {
        this.cargando = false;
        this.errorApi = 'Error al enviar reserva';
      },
    });
  }

  nuevaReserva(): void {
    this.enviado = false;
    this.formulario.reset({ pasajeros: 1, clase: 'economica' });
    this.generarAsientos();
  }
}