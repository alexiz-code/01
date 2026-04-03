import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

interface Destino {
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
export class Reservas {

  formulario: FormGroup;
  enviado = false;
  cargando = false;

  destinos: Destino[] = [
    { valor: 'pampa de quinua', nombre: 'pampa de quinua' },
    { valor: 'machu pichu', nombre: 'machu pichu'},
    { valor: 'cusco',  nombre: 'cusco'},
    { valor: 'lago titicaca', nombre: 'lago titicaca'},
    { valor: 'sarhua', nombre: 'sarhua'},
  ];

  constructor(private fb: FormBuilder) {
    this.formulario = this.fb.group({
      nombre:    ['', [Validators.required, Validators.minLength(3)]],
      apellido:  ['', [Validators.required, Validators.minLength(3)]],
      email:     ['', [Validators.required, Validators.email]],
      telefono:  ['', [Validators.required, Validators.pattern(/^\d{9,15}$/)]],
      destino:   ['', Validators.required],
      fechaIda:  ['', Validators.required],
      fechaVuelta: ['', Validators.required],
      pasajeros: [1,  [Validators.required, Validators.min(1), Validators.max(20)]],
      clase:     ['economica', Validators.required],
      notas:     [''],
    });
  }

  // Helpers para validación en template
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

    // Simula llamada al servidor
    setTimeout(() => {
      this.cargando = false;
      this.enviado = true;
      console.log('Reserva:', this.formulario.value);
    }, 1500);
  }

  nuevaReserva(): void {
    this.enviado = false;
    this.formulario.reset({ pasajeros: 1, clase: 'economica' });
  }
}