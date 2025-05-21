import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../models/login-request.model';
import { Observable } from 'rxjs';
import { AuthResponse } from '../models/auth-response.model';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8082/api/usuarios';

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<AuthResponse> {
  return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
    tap((res) => {
      this.guardarToken(res.token);
      localStorage.setItem('usuario', JSON.stringify(res.usuario));
    })
  );
}


  guardarToken(token: string) {
    localStorage.setItem('jwt', token);
  }

  obtenerToken(): string | null {
    return localStorage.getItem('jwt');
  }

  registrar(nuevoUsuario: any) {
  return this.http.post(`${this.apiUrl}/registro`, nuevoUsuario);
}


  cerrarSesion() {
    localStorage.removeItem('jwt');
  }
}