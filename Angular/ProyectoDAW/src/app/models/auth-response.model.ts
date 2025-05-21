export interface AuthResponse {
  token: string;
  usuario: {
    id: number;
    nombre: string;
    correo: string;
    avatar: string;
    rol: {
      nombre: string;
    };
  };
}