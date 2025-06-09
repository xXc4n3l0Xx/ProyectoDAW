// Representa un usuario completo dentro del sistema (tal como lo maneja el backend)
export interface Usuario {
  id: number;                 // ID único del usuario
  nombre: string;             // Nombre del usuario
  correo: string;             // Correo electrónico del usuario
  contrasena: string;         // Contraseña del usuario (generalmente encriptada)
  avatar: string;             // URL o ruta del avatar del usuario
  rol: {                      // Objeto que representa el rol asignado al usuario
    nombre: string;           // Nombre del rol (por ejemplo: 'admin', 'usuario', etc.)
  };
}
