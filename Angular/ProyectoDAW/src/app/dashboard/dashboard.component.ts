// Importa decorador para definir un componente de Angular
import { Component } from '@angular/core';
// Permite navegación programática entre rutas
import { Router } from '@angular/router';
// Módulo común con directivas como *ngIf, *ngFor
import { CommonModule } from '@angular/common';
// Modelo de datos del usuario
import { Usuario } from '../models/usuario.model';
// Servicio de autenticación (login, logout, validación de token)
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-dashboard', // Selector del componente
  standalone: true, // Componente independiente (sin necesidad de módulo)
  templateUrl: './dashboard.component.html', // Ruta al HTML de la vista
  styleUrls: ['./dashboard.component.css'], // Estilos CSS asociados
  imports: [CommonModule] // Módulos necesarios para la vista
})
export class DashboardComponent {
  // Variable para almacenar los datos del usuario actual
  usuario: Usuario | null = null;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    // Carga los datos del usuario almacenados en localStorage
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      this.usuario = JSON.parse(usuarioGuardado) as Usuario;
    }

    // Si el token está expirado, cerrar sesión y redirigir al inicio con mensaje
    if (this.authService.isTokenExpirado()) {
      this.authService.cerrarSesion();
      this.router.navigate(['/'], { queryParams: { expirado: '1' } });
    }
  }

  // Abre el juego en una nueva pestaña si el token es válido
  jugar() {
    if (this.authService.isTokenExpirado()) {
      this.authService.cerrarSesion();
      this.router.navigate(['/'], { queryParams: { expirado: '1' } });
    } else {
      window.open('/assets/DAWdventures/GoDAWt.html', '_blank');
    }
  }

  // Redirige al foro si el token es válido
  irAlForo() {
    if (this.authService.isTokenExpirado()) {
      this.authService.cerrarSesion();
      this.router.navigate(['/'], { queryParams: { expirado: '1' } });
    } else {
      this.router.navigate(['/foro']);
    }
  }

  // Redirige al perfil del usuario si el token es válido
  editarCuenta() {
    if (this.authService.isTokenExpirado()) {
      this.authService.cerrarSesion();
      this.router.navigate(['/'], { queryParams: { expirado: '1' } });
    } else {
      this.router.navigate(['/perfil']);
    }
  }

  // Cierra la sesión y redirige al inicio
  cerrarSesion() {
    this.authService.cerrarSesion();
    this.router.navigate(['/']);
  }

  // Redirige a la vista de usuarios (visible solo para admins)
  verUsuarios() {
    this.router.navigate(['/usuarios']);
  }

  // Redirige a la wiki del juego o app
  irAWiki() {
    this.router.navigate(['/wiki']);
  }
}
