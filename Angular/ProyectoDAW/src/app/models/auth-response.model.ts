// Define la interfaz de la respuesta del backend al iniciar sesión (login)
export interface AuthResponse {
  token: string; // Token JWT devuelto por el servidor para autenticación

  usuario: {     // Objeto con la información del usuario autenticado
    id: number;         // ID único del usuario
    nombre: string;     // Nombre del usuario
    correo: string;     // Correo del usuario
    avatar: string;     // Ruta o URL del avatar del usuario
    rol: {
      nombre: string;   // Nombre del rol (por ejemplo: 'admin' o 'usuario')
    };
  };
}
