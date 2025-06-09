// Importa herramientas necesarias para pruebas unitarias en Angular
import { ComponentFixture, TestBed } from '@angular/core/testing';

// Importa el componente a probar
import { DashboardComponent } from './dashboard.component';

// Conjunto de pruebas para el componente DashboardComponent
describe('DashboardComponent', () => {
  // Variables para manejar la instancia del componente y su fixture (entorno de pruebas)
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  // Configura el entorno de pruebas antes de cada test
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // Importa directamente el componente standalone
      imports: [DashboardComponent]
    })
    .compileComponents(); // Compila los componentes para su uso en pruebas

    // Crea una instancia del componente dentro del entorno de pruebas
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // Aplica la detección de cambios (ngOnInit, bindings, etc.)
  });

  // Prueba básica: verifica que el componente se crea correctamente
  it('should create', () => {
    expect(component).toBeTruthy(); // Verifica que la instancia del componente no sea nula
  });
});
