// Importa herramientas básicas para crear el componente
import { Component, OnInit } from '@angular/core';
// Importa herramientas para leer parámetros de la URL y navegar entre rutas
import { ActivatedRoute, Router } from '@angular/router';
// Directivas comunes como *ngIf, *ngFor
import { CommonModule } from '@angular/common';
// Para el manejo de formularios
import { FormsModule } from '@angular/forms';
// Cliente HTTP y encabezados para peticiones seguras
import { HttpClient, HttpHeaders } from '@angular/common/http';
// Modelo de datos para los posts del foro
import { Post } from '../models/post.model';
// URL base del backend
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-hilo', // Selector del componente
  standalone: true, // No necesita declararse en un módulo
  templateUrl: './hilo.component.html', // Vista HTML
  styleUrls: ['./hilo.component.css'], // Estilos CSS
  imports: [CommonModule, FormsModule] // Módulos necesarios para el template
})
export class HiloComponent implements OnInit {
  // Variables del componente
  idHilo!: number; // ID del hilo actual
  posts: Post[] = []; // Posts activos
  postsEliminados: Post[] = []; // Posts eliminados (visible solo para admin)
  nuevoPost: string = ''; // Contenido del nuevo post
  usuarioLogueado: { id: number; nombre: string; rol: { nombre: string } } | null = null; // Usuario actual
  tituloHilo: string = ''; // Título del hilo
  modoEdicion: { [postId: number]: boolean } = {}; // Control de edición por post
  contenidoEditado: { [postId: number]: string } = {}; // Contenido editado por post

  constructor(
    private route: ActivatedRoute, // Para obtener parámetros de la URL
    private router: Router,        // Para redirigir a otras rutas
    private http: HttpClient       // Cliente para hacer peticiones HTTP
  ) {}

  // Se ejecuta al cargar el componente
  ngOnInit(): void {
    this.idHilo = Number(this.route.snapshot.paramMap.get('id')); // Obtiene el ID del hilo desde la URL

    const guardado = localStorage.getItem('usuario');
    if (guardado) {
      this.usuarioLogueado = JSON.parse(guardado); // Carga el usuario logueado
    }

    this.cargarPosts();         // Carga los posts del hilo
    this.obtenerTituloHilo();  // Carga el título del hilo
  }

  // Carga los posts activos y eliminados (si es admin)
  cargarPosts() {
    const token = localStorage.getItem('jwt');
    if (!token) return;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.get<Post[]>(`${environment.apiUrl}/posts/thread/${this.idHilo}`, { headers }).subscribe({
      next: (res) => this.posts = res,
      error: () => alert('Error al cargar los posts.')
    });

    if (this.usuarioLogueado?.rol?.nombre.toLowerCase() === 'admin') {
      this.http.get<Post[]>(`${environment.apiUrl}/posts/thread/${this.idHilo}/eliminados`, { headers }).subscribe({
        next: (res) => this.postsEliminados = res,
        error: () => {}
      });
    }
  }

  // Publica un nuevo post en el hilo
  crearPost() {
    const token = localStorage.getItem('jwt');
    if (!token || !this.usuarioLogueado) return;
    if (!this.nuevoPost || this.nuevoPost.trim() === '') return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    const nuevo = {
      idUsuario: this.usuarioLogueado.id,
      idThread: this.idHilo,
      contenido: this.nuevoPost.trim()
    };

    this.http.post(`${environment.apiUrl}/posts`, nuevo, { headers }).subscribe({
      next: () => {
        this.nuevoPost = '';
        this.cargarPosts();
      },
      error: () => alert('Error al publicar')
    });
  }

  // Elimina un post (solo si tiene permiso)
  eliminarPost(idPost: number) {
    const token = localStorage.getItem('jwt');
    if (!token) return;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    if (confirm('¿Seguro que deseas eliminar este post?')) {
      this.http.delete(`${environment.apiUrl}/posts/${idPost}`, { headers }).subscribe({
        next: () => this.cargarPosts(),
        error: () => alert('No puedes eliminar este post')
      });
    }
  }

  // Reactiva un post eliminado (solo admin)
  reactivarPost(idPost: number) {
    const token = localStorage.getItem('jwt');
    if (!token) return;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.put(`${environment.apiUrl}/posts/${idPost}/activar`, {}, { headers }).subscribe({
      next: () => this.cargarPosts(),
      error: () => alert('No se pudo reactivar el post')
    });
  }

  // Verifica si el usuario puede eliminar un post
  puedeEliminar(nombreUsuarioPost: string): boolean {
    if (!this.usuarioLogueado) return false;
    return (
      this.usuarioLogueado.nombre === nombreUsuarioPost ||
      this.usuarioLogueado.rol?.nombre.toLowerCase() === 'admin'
    );
  }

  // Redirige al foro
  volver() {
    this.router.navigate(['/foro']);
  }

  // Redirige a la página de inicio
  inicio() {
    this.router.navigate(['/']);
  }

  // Carga el título del hilo desde la lista de hilos
  obtenerTituloHilo() {
    const token = localStorage.getItem('jwt');
    if (!token) return;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.get<any[]>(`${environment.apiUrl}/hilos`, { headers }).subscribe({
      next: (temas) => {
        const hilo = temas.find(t => t.id === this.idHilo);
        if (hilo) this.tituloHilo = hilo.titulo;
      },
      error: () => alert('No se pudo obtener el título del hilo')
    });
  }

  // Verifica si el usuario puede editar algún post del hilo
  puedeEditarHilo(): boolean {
    if (!this.usuarioLogueado) return false;
    return this.usuarioLogueado.rol?.nombre.toLowerCase() === 'admin' ||
           this.posts.some(post => post.nombreUsuario === this.usuarioLogueado?.nombre);
  }

  // Verifica si el usuario puede editar un post (usa la misma lógica que puedeEliminar)
  puedeEditar(nombreUsuarioPost: string): boolean {
    return this.puedeEliminar(nombreUsuarioPost);
  }

  // Activa el modo de edición para un post específico
  editarPost(id: number, contenido: string) {
    this.modoEdicion[id] = true;
    this.contenidoEditado[id] = contenido;
  }

  // Cancela la edición de un post
  cancelarEdicion(id: number) {
    delete this.modoEdicion[id];
    delete this.contenidoEditado[id];
  }

  // Guarda los cambios de contenido en un post editado
  guardarEdicion(id: number) {
    const token = localStorage.getItem('jwt');
    if (!token || !this.contenidoEditado[id]?.trim()) return;

    const nuevoContenido = this.contenidoEditado[id].trim();
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.put(`${environment.apiUrl}/posts/${id}`, 
      { contenido: nuevoContenido }, { headers }).subscribe({
        next: () => {
          this.cancelarEdicion(id);
          this.cargarPosts();
        },
        error: () => alert('Error al editar el post')
      });
  }
}
