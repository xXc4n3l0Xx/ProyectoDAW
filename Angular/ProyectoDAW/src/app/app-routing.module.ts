// Importa decoradores y módulos de enrutamiento de Angular
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Componentes de la aplicación
import { HomeComponent } from './home/home.component';
import { RegistroComponent } from './auth/registro/registro.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PerfilComponent } from './perfil/perfil.component';
import { ForoComponent } from './foro/foro.component';
import { HiloComponent } from './hilo/hilo.component';
import { UsuariosComponent } from './usuarios/usuarios.component';
import { WikiComponent } from './wiki/wiki.component';

// Guards de protección para rutas (autenticación y permisos)
import { AuthGuard } from './guards/auth.guard';
import { AdminGuard } from './guards/admin.guard';

// Definición de las rutas de la aplicación
const routes: Routes = [
  { path: 'registro', component: RegistroComponent },                         // Ruta para el registro de usuarios
  { path: '', component: HomeComponent },                                     // Ruta principal (home/login)
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard] }, // Solo accesible si el usuario está autenticado
  { path: 'perfil', component: PerfilComponent, canActivate: [AuthGuard] },       // Perfil de usuario (requiere login)
  { path: 'foro', component: ForoComponent, canActivate: [AuthGuard] },           // Foro de discusión (requiere login)
  { path: 'tema/:id', component: HiloComponent, canActivate: [AuthGuard] },       // Hilo individual (requiere login)
  { path: 'wiki', component: WikiComponent },                                      // Página de wiki accesible sin login
  { path: 'usuarios', component: UsuariosComponent, canActivate: [AuthGuard, AdminGuard] }, // Solo accesible por administradores
  { path: '**', redirectTo: '' } // Cualquier ruta no reconocida redirige al home
];

@NgModule({
  imports: [RouterModule.forRoot(routes)], // Registra las rutas en la app
  exports: [RouterModule]                  // Exporta RouterModule para que esté disponible en toda la app
})
export class AppRoutingModule {}
