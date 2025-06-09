// Importa el decorador Injectable para crear un servicio inyectable
import { Injectable } from '@angular/core';
// Importa la interfaz CanActivate para proteger rutas y Router para redirecciones
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root' // Hace que el guard esté disponible en toda la aplicación sin necesidad de importarlo en un módulo
})
export class AdminGuard implements CanActivate {

  constructor(private router: Router) {}

  // Método que determina si la ruta puede activarse
  canActivate(): boolean {
    // Obtiene el usuario desde localStorage
    const usuarioStr = localStorage.getItem('usuario');

    // Si no hay usuario guardado, redirige al inicio
    if (!usuarioStr) {
      this.router.navigate(['/']);
      return false;
    }

    // Parsea el usuario desde JSON
    const usuario = JSON.parse(usuarioStr);

    // Si el rol del usuario es 'admin', permite el acceso
    if (usuario.rol?.nombre?.toLowerCase() === 'admin') {
      return true;
    }

    // Si no es admin, redirige al dashboard
    this.router.navigate(['/dashboard']);
    return false;
  }
}
