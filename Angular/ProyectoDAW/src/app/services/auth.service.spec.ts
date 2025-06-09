// Importa utilidades para configurar pruebas en Angular
import { TestBed } from '@angular/core/testing';

// Importa el servicio que se va a probar
import { AuthService } from './auth.service';

// Define el conjunto de pruebas para AuthService
describe('AuthService', () => {
  let service: AuthService; // Variable donde se almacenará la instancia del servicio

  // Antes de cada prueba, se configura el entorno de pruebas
  beforeEach(() => {
    TestBed.configureTestingModule({}); // Crea un módulo de pruebas vacío
    service = TestBed.inject(AuthService); // Inyecta una instancia del servicio
  });

  // Prueba básica: verifica que el servicio se crea correctamente
  it('should be created', () => {
    expect(service).toBeTruthy(); // Se espera que la instancia del servicio no sea null
  });
});
