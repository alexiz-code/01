import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LoginPayload { email: string; 
                                password: string; }
                                
export interface SessionUser {  id: string; 
                                nombre: string; 
                                email: string; 
                                telefono: string; 
                                rol: string; }

const KEY = 'g7_session';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly http = inject(HttpClient);

    readonly currentUser = signal<SessionUser | null>(this.fromStorage());

    login(payload: LoginPayload): Observable<SessionUser> {
        return this.http.post<SessionUser>(`${environment.apiUrl}/auth/login`, payload).pipe(
            tap(user => {
                this.currentUser.set(user);
                localStorage.setItem(KEY, JSON.stringify(user));
            })
        );
    }

    logout(): void {
        this.currentUser.set(null);
        localStorage.removeItem(KEY);
    }

    get isLoggedIn(): boolean { return this.currentUser() !== null; }
    get isAdmin(): boolean { return this.currentUser()?.rol === 'admin'; }
    get userName(): string { return this.currentUser()?.nombre ?? 'Usuario'; }
    get userRole(): string { return this.currentUser()?.rol ?? ''; }

    private fromStorage(): SessionUser | null {
        try {
            const raw = localStorage.getItem(KEY);
            return raw ? JSON.parse(raw) : null;
        } catch { return null; }
    }
}