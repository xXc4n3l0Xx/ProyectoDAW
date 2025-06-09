// Importa herramientas necesarias para crear componentes en Angular
import { Component, OnInit } from '@angular/core';
// Funciones comunes como *ngIf, *ngFor
import { CommonModule } from '@angular/common';
// Para manejo de formularios
import { FormsModule } from '@angular/forms';
// Cliente HTTP para hacer peticiones al backend
import { HttpClient, HttpHeaders } from '@angular/common/http';
// Navegación entre rutas
import { Router, RouterModule } from '@angular/router';
// Acceso a variables de entorno como la URL base del backend
import { environment } from '../../environments/environment';

// Interfaz que describe la estructura de un tema en el foro
interface Tema {
  id: number;
  titulo: string;
  creadoEn: string;
  nombreUsuario: string;
  avatarUsuario: string;
}

@Component({
  selector: 'app-foro', // Selector del componente
  standalone: true, // Componente independiente (sin módulo)
  templateUrl: './foro.component.html',
  styleUrls: ['./foro.component.css'],
  imports: [CommonModule, FormsModule, RouterModule] // Módulos requeridos
})
export class ForoComponent implements OnInit {
  temas: Tema[] = []; // Lista de temas activos
  temasEliminados: Tema[] = []; // Lista de temas eliminados (solo admin)
  nuevoTitulo: string = ''; // Título del nuevo tema
  usuario: { id: number, nombre: string, rol: { nombre: string } } | null = null; // Usuario autenticado
  editandoTemaId: number | null = null; // ID del tema que se está editando
  tituloEditado: string = ''; // Nuevo título al editar

  constructor(private http: HttpClient, private router: Router) {}

  // Se ejecuta al cargar el componente
  ngOnInit(): void {
    const guardado = localStorage.getItem('usuario');
    if (guardado) {
      this.usuario = JSON.parse(guardado);
    }
    this.cargarTemas(); // Carga los temas desde el backend
  }

  // Carga todos los temas visibles (y eliminados si el usuario es admin)
  cargarTemas() {
    const token = localStorage.getItem('jwt');
    if (!token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    // Carga los temas activos
    this.http.get<Tema[]>(`${environment.apiUrl}/hilos`, { headers }).subscribe({
      next: (res) => this.temas = res,
      error: () => alert('Error al cargar los temas.')
    });

    // Si el usuario es admin, también carga los eliminados
    if (this.usuario?.rol?.nombre.toLowerCase() === 'admin') {
      this.http.get<Tema[]>(`${environment.apiUrl}/hilos/eliminados`, { headers }).subscribe({
        next: (res) => this.temasEliminados = res,
        error: () => {}
      });
    }
  }

  // Crea un nuevo tema en el foro
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

    this.http.post<Tema>(`${environment.apiUrl}/hilos`, nuevoTema, { headers }).subscribe({
      next: () => {
        this.nuevoTitulo = '';
        this.cargarTemas(); // Recarga la lista de temas
      },
      error: () => alert('Error al crear el tema.')
    });
  }

  // Elimina (desactiva) un tema si el usuario tiene permiso
  eliminarTema(idTema: number) {
    const token = localStorage.getItem('jwt');
    if (!this.usuario || !token) return;

    if (!confirm('¿Seguro que deseas eliminar este tema?')) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.delete(`${environment.apiUrl}/hilos/${idTema}/usuario/${this.usuario.id}`, { headers }).subscribe({
      next: () => this.cargarTemas(),
      error: () => alert('No puedes eliminar este tema')
    });
  }

  // Reactiva un tema previamente eliminado (solo para admin)
  reactivarTema(idTema: number) {
    const token = localStorage.getItem('jwt');
    if (!this.usuario || !token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.put(`${environment.apiUrl}/hilos/${idTema}/activar/usuario/${this.usuario.id}`, {}, { headers }).subscribe({
      next: () => this.cargarTemas(),
      error: () => alert('No se pudo reactivar el tema')
    });
  }

  // Verifica si el usuario puede eliminar el tema (es dueño o admin)
  puedeEliminar(nombreUsuarioTema: string): boolean {
    if (!this.usuario) return false;
    return (
      this.usuario.nombre === nombreUsuarioTema ||
      this.usuario.rol?.nombre.toLowerCase() === 'admin'
    );
  }

  // Navega a la vista del tema seleccionado
  irATema(id: number) {
    this.router.navigate(['/tema', id]);
  }

  // Inicia la edición del título de un tema
  iniciarEdicionTitulo(tema: Tema) {
    this.editandoTemaId = tema.id;
    this.tituloEditado = tema.titulo;
  }

  // Cancela la edición del título
  cancelarEdicion() {
    this.editandoTemaId = null;
    this.tituloEditado = '';
  }

  // Guarda el nuevo título del tema editado
  guardarTituloEditado(idTema: number) {
    if (!this.tituloEditado.trim()) {
      alert('El título no puede estar vacío.');
      return;
    }

    const token = localStorage.getItem('jwt');
    if (!this.usuario || !token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.put(`${environment.apiUrl}/hilos/${idTema}/usuario/${this.usuario.id}`,
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

  // Verifica si el usuario puede editar el tema
  puedeEditar(nombreUsuarioTema: string): boolean {
    if (!this.usuario) return false;
    return this.usuario.nombre === nombreUsuarioTema || this.usuario.rol?.nombre.toLowerCase() === 'admin';
  }
}
