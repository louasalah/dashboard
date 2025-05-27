import { Routes } from '@angular/router';
import { StarterComponent } from './starter/starter.component';
import { AppSideLoginComponent } from './authentication/side-login/side-login.component';

export const PagesRoutes: Routes = [
  {
    path: '',
    component: StarterComponent,
    data: {
      title: 'Starter Page',
      urls: [
        { title: 'Dashboard', url: '/dashboards/dashboard1' },
        { title: 'Starter Page' },
      ],
    },
  },
  {
    path: '',
    component: AppSideLoginComponent,
    data: {
      title: 'Login Page',
      urls: [
        { title: 'Login', url: '/login' },
        { title: 'Login Page' },
      ],
    },
  },
];
