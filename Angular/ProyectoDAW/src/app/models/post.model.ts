// Representa un post (comentario o mensaje) dentro de un hilo del foro
export interface Post {
  id: number;               // ID único del post
  contenido: string;        // Texto o mensaje del post
  fechaCreacion: string;    // Fecha en que se creó el post (en formato ISO o string)
  nombreUsuario: string;    // Nombre del usuario que escribió el post
  avatarUsuario: string;    // URL o ruta del avatar del usuario que escribió el post
}
