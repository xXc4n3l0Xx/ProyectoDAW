// Importa el decorador Component para definir componentes en Angular
import { Component } from '@angular/core';
// Permite navegar entre rutas programáticamente
import { Router } from '@angular/router';
// Módulos para el manejo de formularios
import { FormsModule, NgForm } from '@angular/forms';
// Funcionalidades comunes como *ngIf, *ngFor
import { CommonModule } from '@angular/common';
// Servicio personalizado para autenticación de usuarios
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-registro', // Nombre del selector del componente
  standalone: true, // Componente independiente (standalone)
  imports: [CommonModule, FormsModule], // Importa módulos necesarios para plantillas y formularios
  templateUrl: './registro.component.html', // Plantilla HTML del componente
  styleUrls: ['./registro.component.css'] // Estilos del componente
})
export class RegistroComponent {
  // Propiedades del formulario
  nombre: string = '';
  correo: string = '';
  contrasena: string = '';
  avatar: string = '';
  mostrarErrorAvatar: boolean = false;

  // Lista de rutas de imágenes de avatares
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

  // Inyecta el servicio de rutas y autenticación
  constructor(private router: Router, private authService: AuthService) {}

  // Función para seleccionar un avatar
  seleccionarAvatar(icono: string) {
    this.avatar = icono;
    this.mostrarErrorAvatar = false;
  }

  // Función principal para registrar al usuario
  registrar() {
    // Verifica que todos los campos estén llenos
    if (!this.nombre || !this.correo || !this.contrasena) {
      alert("Por favor, completa todos los campos.");
      return;
    }

    // Verifica que se haya seleccionado un avatar
    if (!this.avatar) {
      this.mostrarErrorAvatar = true;
      return;
    }

    // Crea un objeto con los datos del nuevo usuario
    const nuevoUsuario = {
      nombre: this.nombre,
      correo: this.correo,
      contrasena: this.contrasena,
      avatar: this.avatar
    };

    // Llama al servicio para registrar al usuario
    this.authService.registrar(nuevoUsuario).subscribe({
      // Si el registro es exitoso, intenta iniciar sesión automáticamente
      next: () => {
        this.authService.login({
          correo: this.correo,
          contrasena: this.contrasena
        }).subscribe({
          next: (res) => {
            // Guarda el token y los datos del usuario en localStorage
            this.authService.guardarToken(res.token);
            localStorage.setItem('usuario', JSON.stringify(res.usuario));
            this.router.navigate(['/dashboard']); // Redirige al dashboard
          },
          error: () => {
            alert('Registro exitoso, pero hubo un error al iniciar sesión.');
          }
        });
      },
      // Manejo de errores del registro
      error: (err) => {
        console.log("ERROR COMPLETO:", err);
        console.log("err.error:", err.error);

        let mensaje = '';

        // Extrae el mensaje del error dependiendo del tipo
        if (typeof err.error === 'string') {
          mensaje = err.error;
        } else if (err.error?.message) {
          mensaje = err.error.message;
        }

        // Si el error es conflicto (409), muestra mensajes personalizados
        if (err.status === 409) {
          if (!mensaje) {
            mensaje = 'Ya existe un usuario con ese nombre o correo.';
          }

          if (mensaje.includes('correo')) {
            alert('Error: el correo ya está en uso por otro usuario.');
          } else if (mensaje.includes('nombre')) {
            alert('Error: el nombre ya está en uso por otro usuario.');
          } else {
            alert('Error: ' + mensaje);
          }
        } else {
          // Mensaje genérico si no es conflicto
          alert((mensaje || 'Ya existe un usuario con ese nombre o correo'));
        }
      }
    });
  }

  // Función para volver a la página de inicio
  volver() {
    this.router.navigate(['/']);
  }
}
