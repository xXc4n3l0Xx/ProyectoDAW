// Cliente HTTP para hacer peticiones al backend
import { HttpClient } from '@angular/common/http';
// Permite que el servicio se pueda inyectar en cualquier parte de la app
import { Injectable } from '@angular/core';
// Modelo con los datos que se envían al iniciar sesión
import { LoginRequest } from '../models/login-request.model';
// Permite trabajar con flujos de datos asincrónicos
import { Observable } from 'rxjs';
// Modelo con la respuesta esperada al hacer login
import { AuthResponse } from '../models/auth-response.model';
// Operador que permite ejecutar efectos secundarios sin alterar la respuesta
import { tap } from 'rxjs/operators';
// Función para decodificar el token JWT
import { jwtDecode } from 'jwt-decode';
// Acceso a la URL del backend desde variables de entorno
import { environment } from '../../environments/environment';

// Estructura del token decodificado, usada para comprobar si expiró
interface TokenPayload {
  exp: number;              // Timestamp de expiración (en segundos)
  [key: string]: any;       // Otros posibles datos en el token
}

@Injectable({
  providedIn: 'root' // Hace que el servicio esté disponible globalmente
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/usuarios`; // Ruta base para autenticación

  constructor(private http: HttpClient) {}

  // Realiza la solicitud de login al backend
  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((res) => {
        // Guarda el token y los datos del usuario al iniciar sesión
        this.guardarToken(res.token);
        localStorage.setItem('usuario', JSON.stringify(res.usuario));
      })
    );
  }

  // Guarda el token JWT en el localStorage
  guardarToken(token: string) {
    localStorage.setItem('jwt', token);
  }

  // Devuelve el token almacenado en localStorage, si existe
  obtenerToken(): string | null {
    return localStorage.getItem('jwt');
  }

  // Realiza el registro de un nuevo usuario
  registrar(nuevoUsuario: any) {
    return this.http.post(`${this.apiUrl}/registro`, nuevoUsuario);
  }

  // Cierra la sesión del usuario borrando token y datos del localStorage
  cerrarSesion() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('usuario');
  }

  // Verifica si el token está expirado
  isTokenExpirado(): boolean {
    const token = this.obtenerToken();
    if (!token) return true;

    try {
      const payload: TokenPayload = jwtDecode(token); // Decodifica el contenido del token
      const ahora = Math.floor(Date.now() / 1000);     // Timestamp actual en segundos
      return payload.exp < ahora;                      // Compara expiración con el tiempo actual
    } catch (e) {
      return true; // Si hay error al decodificar, se asume que el token es inválido
    }
  }
}
