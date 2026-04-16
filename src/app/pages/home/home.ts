import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { G7ApiService } from '../../core/g7-api.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  private readonly router = inject(Router);
  private readonly api = inject(G7ApiService);

  apiOk: boolean | null = null;

  constructor() {
    this.api.health().subscribe({
      next: (r) => (this.apiOk = r?.status === 'ok'),
      error: () => (this.apiOk = false),
    });
  }

  verDetalle(lugar: string): void {
    this.router.navigate(['/detalle', lugar]);
  }
}
