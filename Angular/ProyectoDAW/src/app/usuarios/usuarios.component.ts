// Herramientas esenciales para definir un componente
import { Component, OnInit } from '@angular/core';
// Módulo para directivas como *ngIf, *ngFor
import { CommonModule } from '@angular/common';
// Cliente HTTP para peticiones al backend y configuración de cabeceras
import { HttpClient, HttpHeaders } from '@angular/common/http';
// Para navegación y rutas en la vista
import { RouterModule } from '@angular/router';
// Acceso a la URL base del backend
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-usuarios', // Nombre del componente en el HTML
  standalone: true,         // Componente independiente (no necesita módulo externo)
  templateUrl: './usuarios.component.html', // Archivo HTML asociado
  styleUrls: ['./usuarios.component.css'],  // Estilos CSS
  imports: [CommonModule, RouterModule]     // Módulos utilizados en el template
})
export class UsuariosComponent implements OnInit {
  usuarios: any[] = []; // Lista de usuarios obtenida del backend
  usuarioActual: { id: number } | null = null; // Usuario autenticado actualmente

  constructor(private http: HttpClient) {}

  // Se ejecuta al cargar el componente
  ngOnInit(): void {
    // Carga el usuario actual desde localStorage
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      this.usuarioActual = JSON.parse(usuarioGuardado);
    }

    // Obtiene el token para autorizar la petición
    const token = localStorage.getItem('jwt');
    if (!token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    // Obtiene la lista completa de usuarios
    this.http.get<any[]>(`${environment.apiUrl}/usuarios/todos`, { headers }).subscribe({
      next: (res) => this.usuarios = res,
      error: () => alert('No se pudo obtener la lista de usuarios')
    });
  }

  // Cambia el estado de un usuario (activo/inactivo)
  cambiarEstado(id: number, nuevoEstado: number) {
    const token = localStorage.getItem('jwt');
    if (!token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    // Realiza la petición para cambiar el estado del usuario
    this.http.put(`${environment.apiUrl}/usuarios/${id}/estado/${nuevoEstado}`, {}, {
      headers,
      responseType: 'text' // El backend devuelve solo texto
    }).subscribe({
      next: () => this.ngOnInit(), // Recarga la lista de usuarios al actualizar
      error: () => alert('No se pudo actualizar el estado')
    });
  }
}
