import { Routes } from '@angular/router';
import { authGuard } from './core/Auth.guard';


export const routes: Routes = [

  { path: '', redirectTo: 'home', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () => import('./pages/login/login').then(m => m.Login),
  },

  {
    path: 'home',
    loadComponent: () => import('./pages/home/home').then(m => m.Home),
  },

  {
    path: 'destinos',
    loadComponent: () => import('./pages/destinos/destinos').then(m => m.Destinos),
  },

  {
    path: 'reservas',
    loadComponent: () => import('./pages/reservas/reservas').then(m => m.Reservas),
  },

  {
    path: 'contacto',
    loadComponent: () => import('./pages/contacto/contacto').then(m => m.Contacto),
  },

  {
    path: 'users',
    loadComponent: () => import('./pages/users/users').then(m => m.Users),
    canActivate: [authGuard],
  },

  { path: '**', redirectTo: 'login' },
];