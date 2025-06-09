// Importa utilidades de Angular para pruebas unitarias (crear testbed y componentes)
import { ComponentFixture, TestBed } from '@angular/core/testing';

// Importa el componente que se va a probar
import { RegistroComponent } from './registro.component';

// Define el conjunto de pruebas para el componente RegistroComponent
describe('RegistroComponent', () => {
  // Declaración de variables para acceder al componente y su fixture (entorno de pruebas)
  let component: RegistroComponent;
  let fixture: ComponentFixture<RegistroComponent>;

  // Antes de cada prueba, se configura el entorno de pruebas
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // Importa directamente el componente standalone para las pruebas
      imports: [RegistroComponent]
    })
    .compileComponents(); // Compila los componentes

    // Crea la instancia del componente dentro del entorno de pruebas
    fixture = TestBed.createComponent(RegistroComponent);
    component = fixture.componentInstance;
  });

  // Prueba básica: verifica que el componente se crea correctamente
  it('should create', () => {
    expect(component).toBeTruthy(); // Espera que el componente exista (no sea null o undefined)
  });
});
