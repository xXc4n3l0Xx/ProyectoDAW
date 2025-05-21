import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [CommonModule, FormsModule]
})
export class HomeComponent {
  correo: string = '';
  contrasena: string = '';
  error: string = '';

  topJugadores = [
    { nombre: 'Marvin', puntuacion: 1200 },
    { nombre: 'Ana', puntuacion: 950 },
    { nombre: 'Luis', puntuacion: 870 },
    { nombre: 'Carlos', puntuacion: 820 },
    { nombre: 'Valeria', puntuacion: 800 }
  ];

  constructor(private authService: AuthService, private router: Router) {
    const token = this.authService.obtenerToken();
    if (token) {
      this.router.navigate(['/dashboard']);
    }
  }

  onLogin() {
    const datos: LoginRequest = {
      correo: this.correo,
      contrasena: this.contrasena
    };

    this.authService.login(datos).subscribe({
      next: (res: AuthResponse) => {
        localStorage.setItem('jwt', res.token);
        localStorage.setItem('usuario', JSON.stringify(res.usuario));
        this.router.navigate(['/dashboard']);
      },
      error: () => {
        this.error = 'Correo o contraseña incorrectos';
      }
    });
  }

  irARegistro() {
    this.router.navigate(['/registro']);
  }
}