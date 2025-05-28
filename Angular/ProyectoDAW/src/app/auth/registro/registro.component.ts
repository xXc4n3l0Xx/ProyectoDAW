import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent {
  nombre: string = '';
  correo: string = '';
  contrasena: string = '';
  avatar: string = '';
  mostrarErrorAvatar: boolean = false;

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

  constructor(private router: Router, private authService: AuthService) {}

  seleccionarAvatar(icono: string) {
    this.avatar = icono;
    this.mostrarErrorAvatar = false;
  }

  registrar() {
  if (!this.nombre || !this.correo || !this.contrasena) {
    alert("Por favor, completa todos los campos.");
    return;
  }

  if (!this.avatar) {
    this.mostrarErrorAvatar = true;
    return;
  }

    const nuevoUsuario = {
      nombre: this.nombre,
      correo: this.correo,
      contrasena: this.contrasena,
      avatar: this.avatar
    };

    this.authService.registrar(nuevoUsuario).subscribe({
      next: () => {
        this.authService.login({
          correo: this.correo,
          contrasena: this.contrasena
        }).subscribe({
          next: (res) => {
            this.authService.guardarToken(res.token);
            localStorage.setItem('usuario', JSON.stringify(res.usuario));
            this.router.navigate(['/dashboard']);
          },
          error: () => {
            alert('Registro exitoso, pero hubo un error al iniciar sesión.');
          }
        });
      },
      error: (err) => {
  console.log("ERROR COMPLETO:", err);
  console.log("err.error:", err.error);

  let mensaje = '';

  if (typeof err.error === 'string') {
    mensaje = err.error;
  } else if (err.error?.message) {
    mensaje = err.error.message;
  }

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
    alert((mensaje || 'Ya existe un usuario con ese nombre o correo'));
  }
}

    });
  }

  volver() {
    this.router.navigate(['/']);
  }
}
