import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean {
    const usuarioStr = localStorage.getItem('usuario');

    if (!usuarioStr) {
      this.router.navigate(['/']);
      return false;
    }

    const usuario = JSON.parse(usuarioStr);
    if (usuario.rol?.nombre?.toLowerCase() === 'admin') {
      return true;
    }

    this.router.navigate(['/dashboard']);
    return false;
  }
}
