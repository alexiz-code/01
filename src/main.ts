import { bootstrapApplication } from '@angular/platform-browser';
import { initializeApp } from 'firebase/app';

import { appConfig } from './app/app.config';
import { AppComponent } from './app/app';
import { environment } from './environments/environment';

initializeApp(environment.firebase);

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));