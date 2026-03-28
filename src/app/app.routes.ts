import { Routes } from '@angular/router';

import { Home } from './pages/home/home';
import { Destinos } from './pages/destinos/destinos';
import { Reservas } from './pages/reservas/reservas';
import { Contacto} from './pages/contacto/contacto';

export const routes = [
  { path: 'home', component: Home },
  { path: 'destinos', component: Destinos },
  { path: 'reservas', component: Reservas },
  { path: 'contacto', component: Contacto }
];