import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Post } from '../models/post.model';

@Component({
  selector: 'app-hilo',
  standalone: true,
  templateUrl: './hilo.component.html',
  styleUrls: ['./hilo.component.css'],
  imports: [CommonModule, FormsModule]
})
export class HiloComponent implements OnInit {
  idHilo!: number;
  posts: Post[] = [];
  postsEliminados: Post[] = [];
  nuevoPost: string = '';
  usuarioLogueado: { id: number; nombre: string; rol: { nombre: string } } | null = null;
  tituloHilo: string = '';
  modoEdicion: { [postId: number]: boolean } = {};
  contenidoEditado: { [postId: number]: string } = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.idHilo = Number(this.route.snapshot.paramMap.get('id'));
    const guardado = localStorage.getItem('usuario');
    if (guardado) {
      this.usuarioLogueado = JSON.parse(guardado);
    }
    this.cargarPosts();
    this.obtenerTituloHilo();
  }

  cargarPosts() {
    const token = localStorage.getItem('jwt');
    if (!token) return;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.get<Post[]>(`http://localhost:8082/api/posts/thread/${this.idHilo}`, { headers }).subscribe({
      next: (res) => this.posts = res,
      error: () => alert('Error al cargar los posts.')
    });

    if (this.usuarioLogueado?.rol?.nombre.toLowerCase() === 'admin') {
      this.http.get<Post[]>(`http://localhost:8082/api/posts/thread/${this.idHilo}/eliminados`, { headers }).subscribe({
        next: (res) => this.postsEliminados = res,
        error: () => {}
      });
    }
  }

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

    this.http.post(`http://localhost:8082/api/posts`, nuevo, { headers }).subscribe({
      next: () => {
        this.nuevoPost = '';
        this.cargarPosts();
      },
      error: () => alert('Error al publicar')
    });
  }

  eliminarPost(idPost: number) {
    const token = localStorage.getItem('jwt');
    if (!token) return;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    if (confirm('¿Seguro que deseas eliminar este post?')) {
      this.http.delete(`http://localhost:8082/api/posts/${idPost}`, { headers }).subscribe({
        next: () => this.cargarPosts(),
        error: () => alert('No puedes eliminar este post')
      });
    }
  }

  reactivarPost(idPost: number) {
    const token = localStorage.getItem('jwt');
    if (!token) return;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.put(`http://localhost:8082/api/posts/${idPost}/activar`, {}, { headers }).subscribe({
      next: () => this.cargarPosts(),
      error: () => alert('No se pudo reactivar el post')
    });
  }

  puedeEliminar(nombreUsuarioPost: string): boolean {
    if (!this.usuarioLogueado) return false;
    return (
      this.usuarioLogueado.nombre === nombreUsuarioPost ||
      this.usuarioLogueado.rol?.nombre.toLowerCase() === 'admin'
    );
  }

  volver() {
    this.router.navigate(['/foro']);
  }

  inicio() {
    this.router.navigate(['/']);
  }

  obtenerTituloHilo() {
    const token = localStorage.getItem('jwt');
    if (!token) return;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.get<any[]>('http://localhost:8082/api/hilos', { headers }).subscribe({
      next: (temas) => {
        const hilo = temas.find(t => t.id === this.idHilo);
        if (hilo) this.tituloHilo = hilo.titulo;
      },
      error: () => alert('No se pudo obtener el título del hilo')
    });
  }

  puedeEditarHilo(): boolean {
    if (!this.usuarioLogueado) return false;
    return this.usuarioLogueado.rol?.nombre.toLowerCase() === 'admin' ||
           this.posts.some(post => post.nombreUsuario === this.usuarioLogueado?.nombre);
  }

  puedeEditar(nombreUsuarioPost: string): boolean {
    return this.puedeEliminar(nombreUsuarioPost);
  }

  editarPost(id: number, contenido: string) {
    this.modoEdicion[id] = true;
    this.contenidoEditado[id] = contenido;
  }

  cancelarEdicion(id: number) {
    delete this.modoEdicion[id];
    delete this.contenidoEditado[id];
  }

  guardarEdicion(id: number) {
    const token = localStorage.getItem('jwt');
    if (!token || !this.contenidoEditado[id]?.trim()) return;

    const nuevoContenido = this.contenidoEditado[id].trim();
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.put(`http://localhost:8082/api/posts/${id}`, 
      { contenido: nuevoContenido }, { headers }).subscribe({
        next: () => {
          this.cancelarEdicion(id);
          this.cargarPosts();
        },
        error: () => alert('Error al editar el post')
      });
  }
}
