import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RegistroComponent } from './auth/registro/registro.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PerfilComponent } from './perfil/perfil.component';
import { AuthGuard } from './guards/auth.guard';
import { ForoComponent } from './foro/foro.component';
import { HiloComponent } from './hilo/hilo.component';
import { UsuariosComponent } from './usuarios/usuarios.component';
import { WikiComponent } from './wiki/wiki.component';
import { AdminGuard } from './guards/admin.guard';

const routes: Routes = [
  { path: 'registro', component: RegistroComponent },
  { path: '', component: HomeComponent },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] },
  { path: 'perfil', component: PerfilComponent, canActivate: [AuthGuard] },
  { path: 'foro', component: ForoComponent, canActivate: [AuthGuard] },
  { path: 'tema/:id', component: HiloComponent, canActivate: [AuthGuard] },
  { path: 'wiki', component: WikiComponent },
  { path: 'usuarios', component: UsuariosComponent, canActivate: [AuthGuard, AdminGuard] },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
