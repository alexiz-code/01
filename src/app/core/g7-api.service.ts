import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UserDto {
  id: string;
  nombre: string;
  email: string;
  telefono: string;
  rol: 'admin' | 'cliente';
  reservas: number;
}

export interface UserCreatePayload {
  nombre: string;
  email: string;
  telefono: string;
  rol: 'admin' | 'cliente';
  reservas: number;
}

export interface DestinoDto {
  id: string;
  label: string;
  title: string;
  desc: string;
  name: string;
  bg: string;
  thumb: string;
}

export interface ReservaCreatePayload {
  nombre: string;
  apellido: string;
  email: string;
  telefono: string;
  destino: string;
  fechaIda: string;
  fechaVuelta: string;
  pasajeros: number;
  clase: string;
  notas: string;
}

export interface ContactoCreatePayload {
  nombre: string;
  email: string;
  mensaje: string;
}

@Injectable({ providedIn: 'root' })
export class G7ApiService {
  private readonly http = inject(HttpClient);
  private readonly base = environment.apiUrl;

  health(): Observable<{ status: string }> {
    return this.http.get<{ status: string }>(`${this.base}/health`);
  }

  getUsers(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>(`${this.base}/users`);
  }

  createUser(body: UserCreatePayload): Observable<UserDto> {
    return this.http.post<UserDto>(`${this.base}/users`, body);
  }

  updateUser(id: string, body: UserCreatePayload): Observable<UserDto> {
    return this.http.put<UserDto>(`${this.base}/users/${encodeURIComponent(id)}`, body);
  }

  deleteUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/users/${encodeURIComponent(id)}`);
  }

  getDestinos(): Observable<DestinoDto[]> {
    return this.http.get<DestinoDto[]>(`${this.base}/destinos`);
  }

  createReserva(body: ReservaCreatePayload): Observable<unknown> {
    return this.http.post(`${this.base}/reservas`, body);
  }

  createContacto(body: ContactoCreatePayload): Observable<unknown> {
    return this.http.post(`${this.base}/contacto`, body);
  }
}
