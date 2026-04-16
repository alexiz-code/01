import { Routes } from '@angular/router';

import { Home } from './pages/home/home';
import { Destinos } from './pages/destinos/destinos';
import { Reservas } from './pages/reservas/reservas';
import { Contacto} from './pages/contacto/contacto';
import { Users } from './pages/users/users';

export const routes = [
  { path: '', pathMatch: 'full' as const, redirectTo: 'home' },
  { path: 'home', component: Home },
  { path: 'destinos', component: Destinos },
  { path: 'reservas', component: Reservas },
  { path: 'contacto', component: Contacto },
  { path: 'users', component: Users }
];