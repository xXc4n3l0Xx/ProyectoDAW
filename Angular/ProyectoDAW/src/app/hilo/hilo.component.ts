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

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    const nuevo = {
      idUsuario: this.usuarioLogueado.id,
      idThread: this.idHilo,
      contenido: this.nuevoPost
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
}
