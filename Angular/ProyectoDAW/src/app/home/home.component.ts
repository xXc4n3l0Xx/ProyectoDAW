// Importa herramientas esenciales de Angular
import { Component } from '@angular/core';
// Router para navegación y ActivatedRoute para leer parámetros de la URL
import { Router, ActivatedRoute } from '@angular/router';
// Servicio de autenticación para login, validación de token, etc.
import { AuthService } from '../services/auth.service';
// Modelo con los datos necesarios para login (correo y contraseña)
import { LoginRequest } from '../models/login-request.model';
// Modelo que representa la respuesta del backend al hacer login
import { AuthResponse } from '../models/auth-response.model';
// Módulos necesarios para el template
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
// Cliente HTTP para llamadas al backend
import { HttpClient, HttpClientModule } from '@angular/common/http';
// Variables de entorno, como la URL base de la API
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-home', // Nombre del componente en el template HTML
  standalone: true, // Componente independiente, sin módulo
  templateUrl: './home.component.html', // Vista HTML
  styleUrls: ['./home.component.css'], // Estilos CSS
  imports: [CommonModule, FormsModule, HttpClientModule] // Módulos utilizados por el componente
})
export class HomeComponent {
  // Propiedades para el formulario de login
  identificador: string = '';
  contrasena: string = '';
  error: string = '';

  // Control de mensaje de expiración de sesión
  mensajeExpiracion: string = '';
  mensajeExpiracionVisible: boolean = false;

  // Lista de los mejores jugadores para mostrar en el top
  topJugadores: { nombre: string; avatar: string; puntuacion: number }[] = [];

  // Control para mostrar una introducción animada por 3 segundos
  mostrarIntro: boolean = true;

  constructor(
    private authService: AuthService,
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Si ya hay un token válido, redirige al dashboard
    const token = this.authService.obtenerToken();
    if (token && !this.authService.isTokenExpirado()) {
      this.router.navigate(['/dashboard']);
    }

    // Verifica si se accede con el parámetro "expirado=1" en la URL
    this.route.queryParams.subscribe(params => {
      if (params['expirado'] === '1') {
        this.mensajeExpiracion = 'Tu sesión ha expirado. Por favor, inicia sesión de nuevo.';
        this.mensajeExpiracionVisible = true;
        setTimeout(() => {
          this.mensajeExpiracionVisible = false;
        }, 5000); // Oculta el mensaje después de 5 segundos
      }
    });

    // Carga el top de jugadores
    this.cargarTopJugadores();

    // Oculta la introducción después de 3 segundos
    setTimeout(() => {
      this.mostrarIntro = false;
    }, 3000);
  }

  // Hace una petición al backend para obtener el top de jugadores
  cargarTopJugadores() {
    this.http.get<any[]>(`${environment.apiUrl}/usuarios/top`).subscribe({
      next: (res) => {
        this.topJugadores = res;
        // Rellena con espacios vacíos si hay menos de 5 jugadores
        while (this.topJugadores.length < 5) {
          this.topJugadores.push({ nombre: '---', avatar: '', puntuacion: 0 });
        }
      },
      error: () => {
        // En caso de error, muestra una lista vacía por defecto
        this.topJugadores = Array(5).fill({ nombre: '---', avatar: '', puntuacion: 0 });
      }
    });
  }

  // Envía los datos del formulario para hacer login
  onLogin() {
    const datos: LoginRequest = {
      correo: this.identificador,
      contrasena: this.contrasena
    };

    // Intenta iniciar sesión
    this.authService.login(datos).subscribe({
      next: (res: AuthResponse) => {
        // Guarda el token y los datos del usuario en localStorage
        localStorage.setItem('jwt', res.token);
        localStorage.setItem('usuario', JSON.stringify(res.usuario));
        this.router.navigate(['/dashboard']); // Redirige al dashboard
      },
      error: () => {
        // Muestra mensaje de error si el login falla
        this.error = 'Error al iniciar sesión, verifique sus credenciales o el estado de su cuenta';
      }
    });
  }

  // Redirige al formulario de registro
  irARegistro() {
    this.router.navigate(['/registro']);
  }
}
