// Define la estructura de los datos necesarios para actualizar un usuario
export interface UsuarioUpdate {
  id: number;           // ID único del usuario que se va a actualizar
  nombre: string;       // Nuevo nombre del usuario
  correo: string;       // Nuevo correo electrónico del usuario
  contrasena?: string;  // Contraseña opcional (solo si se desea cambiar)
  avatar: string;       // Ruta o URL del nuevo avatar del usuario
}
