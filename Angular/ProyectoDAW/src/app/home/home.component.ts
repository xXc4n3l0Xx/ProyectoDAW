import { Component } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { LoginRequest } from '../models/login-request.model';
import { AuthResponse } from '../models/auth-response.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [CommonModule, FormsModule, HttpClientModule]
})
export class HomeComponent {
  correo: string = '';
  contrasena: string = '';
  error: string = '';

  mensajeExpiracion: string = '';
  mensajeExpiracionVisible: boolean = false;

  topJugadores: { nombre: string; avatar: string; puntuacion: number }[] = [];

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {
    const token = this.authService.obtenerToken();
    if (token && !this.authService.isTokenExpirado()) {
      this.router.navigate(['/dashboard']);
    }

    this.route.queryParams.subscribe(params => {
      if (params['expirado'] === '1') {
        this.mensajeExpiracion = 'Tu sesión ha expirado. Por favor, inicia sesión de nuevo.';
        this.mensajeExpiracionVisible = true;
        setTimeout(() => {
          this.mensajeExpiracionVisible = false;
        }, 5000);
      }
    });

    this.cargarTopJugadores();
  }

  cargarTopJugadores() {
    this.http.get<any[]>('http://localhost:8082/api/usuarios/top').subscribe({
      next: (res) => {
        this.topJugadores = res;

        while (this.topJugadores.length < 5) {
          this.topJugadores.push({
            nombre: '---',
            avatar: '',
            puntuacion: 0
          });
        }
      },
      error: () => {
        this.topJugadores = Array(5).fill({
          nombre: '---',
          avatar: '',
          puntuacion: 0
        });
      }
    });
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
