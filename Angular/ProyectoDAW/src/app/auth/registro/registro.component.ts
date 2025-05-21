import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
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
  }

  registrar() {
  if (!this.avatar) {
    alert('Por favor selecciona un avatar.');
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
      alert('Error al registrar: ' + (err.error?.message || 'Ya existe el usuario o correo'));
    }
  });
}


  volver() {
    this.router.navigate(['/']);
  }
}
