import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.css'],
  imports: [CommonModule, RouterModule]
})
export class UsuariosComponent implements OnInit {
  usuarios: any[] = [];
  usuarioActual: { id: number } | null = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      this.usuarioActual = JSON.parse(usuarioGuardado);
    }
    const token = localStorage.getItem('jwt');
    if (!token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    this.http.get<any[]>('http://localhost:8082/api/usuarios/todos', { headers }).subscribe({
      next: (res) => this.usuarios = res,
      error: () => alert('No se pudo obtener la lista de usuarios')
    });
  }

  cambiarEstado(id: number, nuevoEstado: number) {
    const token = localStorage.getItem('jwt');
    if (!token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    this.http.put(`http://localhost:8082/api/usuarios/${id}/estado/${nuevoEstado}`, {}, {
  headers,
  responseType: 'text'
}).subscribe({
  next: () => this.ngOnInit(),
  error: () => alert('No se pudo actualizar el estado')
});

  }
}
