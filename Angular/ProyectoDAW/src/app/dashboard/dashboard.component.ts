import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Usuario } from '../models/usuario.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  imports: [CommonModule]
})
export class DashboardComponent {
  usuario: Usuario | null = null;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      this.usuario = JSON.parse(usuarioGuardado);
    } else {
      this.router.navigate(['/']);
    }
  }

  jugar() {
  window.location.href = '/assets/DAWdventures/GoDAWt.html';
}


  irAlForo() {
    this.router.navigate(['/foro']);
  }

  editarCuenta() {
    this.router.navigate(['/perfil']);
  }

  cerrarSesion() {
    this.authService.cerrarSesion();
    this.router.navigate(['/']);
  }
}
