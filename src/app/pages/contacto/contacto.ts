import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { G7ApiService, ContactoCreatePayload } from '../../core/g7-api.service';

@Component({
  selector: 'app-contacto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './contacto.html',
  styleUrl: './contacto.css',
})
export class Contacto {
  private readonly fb = inject(FormBuilder);
  private readonly api = inject(G7ApiService);

  form: FormGroup = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    mensaje: ['', [Validators.required, Validators.minLength(5)]],
  });

  enviado = false;
  cargando = false;
  errorApi: string | null = null;

  invalido(name: string): boolean {
    const c = this.form.get(name);
    return !!(c && c.invalid && (c.dirty || c.touched));
  }

  enviar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.cargando = true;
    this.errorApi = null;

    const v = this.form.getRawValue();
    const payload: ContactoCreatePayload = {
      nombre: v.nombre,
      email: v.email,
      mensaje: v.mensaje,
    };

    this.api.createContacto(payload).subscribe({
      next: () => {
        this.cargando = false;
        this.enviado = true;
        this.form.reset();
      },
      error: () => {
        this.cargando = false;
        this.errorApi =
          'No se pudo enviar el mensaje. Comprueba que el backend esté ejecutándose.';
      },
    });
  }

  otroMensaje(): void {
    this.enviado = false;
    this.errorApi = null;
  }
}
