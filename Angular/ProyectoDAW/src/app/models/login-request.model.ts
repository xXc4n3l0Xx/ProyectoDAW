// Define la estructura de los datos que se envían al backend para iniciar sesión
export interface LoginRequest {
  correo: string;       // Correo electrónico del usuario
  contrasena: string;   // Contraseña del usuario
}
