import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-perfil',
  standalone: true,
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css'],
  imports: [CommonModule, FormsModule]
})
export class PerfilComponent {
  usuario = {
    id: 0,
    nombre: '',
    correo: '',
    contrasena: '',
    avatar: ''
  };

  avatares: string[] = [
    'assets/icons/icono1.png',
    'assets/icons/icono2.png',
    'assets/icons/icono3.png',
    'assets/icons/icono4.png',
    'assets/icons/icono5.png',
    'assets/icons/icono6.png',
    'assets/icons/icono7.png',
    'assets/icons/icono8.png',
    'assets/icons/icono9.png',
    'assets/icons/icono10.png',
    'assets/icons/icono11.png',
    'assets/icons/icono12.png',
    'assets/icons/icono13.png',
    'assets/icons/icono14.png'
  ];

  constructor(
    private router: Router,
    private authService: AuthService,
    private http: HttpClient
  ) {
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      const datos = JSON.parse(usuarioGuardado);
      this.usuario = {
        id: datos.id,
        nombre: datos.nombre,
        correo: datos.correo,
        contrasena: '',
        avatar: datos.avatar
      };
    }
  }

  seleccionarAvatar(icono: string) {
    this.usuario.avatar = icono;
  }

actualizarPerfil() {
  const token = this.authService.obtenerToken();
  if (!token) {
    alert("No autorizado");
    this.router.navigate(['/']);
    return;
  }

  const datosActualizados: any = {
    id: this.usuario.id,
    nombre: this.usuario.nombre,
    correo: this.usuario.correo,
    contrasena: this.usuario.contrasena,
    avatar: this.usuario.avatar
  };

  this.http.put(`http://localhost:8082/api/usuarios/${this.usuario.id}`, datosActualizados, {
    headers: { Authorization: `Bearer ${token}` }
  }).subscribe({
    next: () => {
      alert('Datos actualizados correctamente. Debes volver a iniciar sesión.');
      this.authService.cerrarSesion();
      this.router.navigate(['/']);
    },
    error: (err) => {
      alert('Error al actualizar: ' + (err.error.message || 'Intenta más tarde'));
    }
  });
}



  cancelar() {
    this.router.navigate(['/dashboard']);
  }
}
