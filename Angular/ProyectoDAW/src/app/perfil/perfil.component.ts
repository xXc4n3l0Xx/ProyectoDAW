// Importa lo necesario para crear un componente
import { Component } from '@angular/core';
// Para navegar entre rutas
import { Router } from '@angular/router';
// Directivas comunes de Angular
import { CommonModule } from '@angular/common';
// Módulos para formularios
import { FormsModule, NgForm } from '@angular/forms';
// Servicio de autenticación (obtener token, cerrar sesión, etc.)
import { AuthService } from '../services/auth.service';
// Cliente HTTP para hacer peticiones al backend
import { HttpClient } from '@angular/common/http';
// URL base del backend
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-perfil', // Nombre del componente
  standalone: true,       // Componente independiente
  templateUrl: './perfil.component.html', // Archivo HTML asociado
  styleUrls: ['./perfil.component.css'],  // Estilos CSS asociados
  imports: [CommonModule, FormsModule]    // Módulos necesarios
})
export class PerfilComponent {
  // Objeto con los datos del usuario a editar
  usuario = {
    id: 0,
    nombre: '',
    correo: '',
    contrasena: '', // Solo se llena si el usuario desea cambiarla
    avatar: ''
  };

  // Bandera para mostrar error si no se selecciona avatar
  mostrarErrorAvatar: boolean = false;

  // Lista de avatares disponibles
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
    // Carga los datos del usuario guardado en localStorage
    const usuarioGuardado = localStorage.getItem('usuario');
    if (usuarioGuardado) {
      const datos = JSON.parse(usuarioGuardado);
      this.usuario = {
        id: datos.id,
        nombre: datos.nombre,
        correo: datos.correo,
        contrasena: '', // Se deja vacío para no mostrarla ni enviarla sin cambios
        avatar: datos.avatar
      };
    }
  }

  // Selecciona un nuevo avatar para el usuario
  seleccionarAvatar(icono: string) {
    this.usuario.avatar = icono;
    this.mostrarErrorAvatar = false;
  }

  // Envía los datos actualizados al backend
  actualizarPerfil(form: NgForm) {
    // Verifica que el formulario sea válido
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    // Verifica que se haya seleccionado un avatar
    if (!this.usuario.avatar) {
      this.mostrarErrorAvatar = true;
      return;
    }

    // Verifica que el usuario esté autenticado
    const token = this.authService.obtenerToken();
    if (!token) {
      alert("No autorizado");
      this.router.navigate(['/']);
      return;
    }

    // Objeto con los datos que se enviarán al backend
    const datosActualizados = {
      id: this.usuario.id,
      nombre: this.usuario.nombre,
      correo: this.usuario.correo,
      contrasena: this.usuario.contrasena,
      avatar: this.usuario.avatar
    };

    // Hace la petición PUT para actualizar el perfil del usuario
    this.http.put(`${environment.apiUrl}/usuarios/${this.usuario.id}`, datosActualizados, {
      headers: { Authorization: `Bearer ${token}` }
    }).subscribe({
      next: () => {
        alert('Datos actualizados correctamente. Debes volver a iniciar sesión.');
        this.authService.cerrarSesion();           // Cierra sesión al actualizar
        this.router.navigate(['/']);               // Redirige al inicio
      },
      error: (err) => {
        alert('Error al registrar: ' + (err.error?.message || 'Ya existe el usuario o correo'));
      }
    });
  }

  // Cancela la edición y vuelve al dashboard
  cancelar() {
    this.router.navigate(['/dashboard']);
  }
}
