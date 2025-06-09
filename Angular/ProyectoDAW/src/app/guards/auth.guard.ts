// Importa el decorador Injectable para que el guard pueda inyectarse como servicio
import { Injectable } from '@angular/core';
// Importa la interfaz CanActivate para proteger rutas, y Router para hacer redirecciones
import { CanActivate, Router } from '@angular/router';
// Importa el servicio de autenticación para acceder al token y funciones de sesión
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root' // Hace que el guard esté disponible en toda la app sin necesidad de declararlo en un módulo
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  // Método que se ejecuta antes de activar una ruta protegida
  canActivate(): boolean {
    const token = this.authService.obtenerToken(); // Obtiene el token JWT

    // Si no hay token o está expirado, cierra sesión y redirige al inicio
    if (!token || this.authService.isTokenExpirado()) {
      this.authService.cerrarSesion();
      this.router.navigate(['/']); // Redirección al home
      return false;
    }

    // Si el token es válido, permite el acceso
    return true;
  }
}
