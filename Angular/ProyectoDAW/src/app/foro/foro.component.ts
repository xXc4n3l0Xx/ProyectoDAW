import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';



interface Tema {
  id: number;
  titulo: string;
  creadoEn: string;
  nombreUsuario: string;
  avatarUsuario: string;
}

@Component({
  selector: 'app-foro',
  standalone: true,
  templateUrl: './foro.component.html',
  styleUrls: ['./foro.component.css'],
  imports: [CommonModule, FormsModule, RouterModule]
})
export class ForoComponent implements OnInit {
  temas: Tema[] = [];
  temasEliminados: Tema[] = [];
  nuevoTitulo: string = '';
  usuario: { id: number, nombre: string, rol: { nombre: string } } | null = null;
  editandoTemaId: number | null = null;
  tituloEditado: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    const guardado = localStorage.getItem('usuario');
    if (guardado) {
      this.usuario = JSON.parse(guardado);
    }
    this.cargarTemas();
  }

  cargarTemas() {
    const token = localStorage.getItem('jwt');
    if (!token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.get<Tema[]>('http://localhost:8082/api/hilos', { headers }).subscribe({
      next: (res) => this.temas = res,
      error: (err) => alert('Error al cargar los temas.')
    });

    if (this.usuario?.rol?.nombre.toLowerCase() === 'admin') {
      this.http.get<Tema[]>('http://localhost:8082/api/hilos/eliminados', { headers }).subscribe({
        next: (res) => this.temasEliminados = res,
        error: () => {}
      });
    }
  }

  crearTema() {
    const token = localStorage.getItem('jwt');
    if (!this.usuario || !token) return;

    if (!this.nuevoTitulo.trim()) {
      alert('Por favor, ingresa un título válido para el tema.');
      return;
    }

    const nuevoTema = {
      idUsuario: this.usuario.id,
      titulo: this.nuevoTitulo.trim()
    };

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.post<Tema>('http://localhost:8082/api/hilos', nuevoTema, { headers }).subscribe({
      next: () => {
        this.nuevoTitulo = '';
        this.cargarTemas();
      },
      error: () => alert('Error al crear el tema.')
    });
  }

  eliminarTema(idTema: number) {
    const token = localStorage.getItem('jwt');
    if (!this.usuario || !token) return;

    if (!confirm('¿Seguro que deseas eliminar este tema?')) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.delete(`http://localhost:8082/api/hilos/${idTema}/usuario/${this.usuario.id}`, { headers }).subscribe({
      next: () => this.cargarTemas(),
      error: () => alert('No puedes eliminar este tema')
    });
  }

  reactivarTema(idTema: number) {
    const token = localStorage.getItem('jwt');
    if (!this.usuario || !token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.put(`http://localhost:8082/api/hilos/${idTema}/activar/usuario/${this.usuario.id}`, {}, { headers }).subscribe({
      next: () => this.cargarTemas(),
      error: () => alert('No se pudo reactivar el tema')
    });
  }

  puedeEliminar(nombreUsuarioTema: string): boolean {
    if (!this.usuario) return false;
    return (
      this.usuario.nombre === nombreUsuarioTema ||
      this.usuario.rol?.nombre.toLowerCase() === 'admin'
    );
  }

  irATema(id: number) {
    this.router.navigate(['/tema', id]);
  }

  iniciarEdicionTitulo(tema: Tema) {
  this.editandoTemaId = tema.id;
  this.tituloEditado = tema.titulo;
}

cancelarEdicion() {
  this.editandoTemaId = null;
  this.tituloEditado = '';
}

guardarTituloEditado(idTema: number) {
  if (!this.tituloEditado.trim()) {
    alert('El título no puede estar vacío.');
    return;
  }

  const token = localStorage.getItem('jwt');
  if (!this.usuario || !token) return;

  const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

  this.http.put(`http://localhost:8082/api/hilos/${idTema}/usuario/${this.usuario.id}`,
    { titulo: this.tituloEditado.trim() },
    { headers }
  ).subscribe({
    next: () => {
      this.cancelarEdicion();
      this.cargarTemas();
    },
    error: () => alert('No se pudo actualizar el título.')
  });
}

puedeEditar(nombreUsuarioTema: string): boolean {
  if (!this.usuario) return false;
  return this.usuario.nombre === nombreUsuarioTema || this.usuario.rol?.nombre.toLowerCase() === 'admin';
}

}
