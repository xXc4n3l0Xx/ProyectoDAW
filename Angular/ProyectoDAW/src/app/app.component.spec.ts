// Importa herramientas para configurar pruebas en Angular
import { TestBed } from '@angular/core/testing';
// Módulo de enrutamiento necesario si AppComponent contiene <router-outlet>
import { RouterModule } from '@angular/router';
// Importa el componente raíz de la aplicación
import { AppComponent } from './app.component';

describe('AppComponent', () => {
  // Se ejecuta antes de cada prueba individual
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // Configura el entorno de pruebas con RouterModule vacío (para evitar errores si se usa <router-outlet>)
      imports: [
        RouterModule.forRoot([]) 
      ],
      declarations: [
        AppComponent // Declara el componente que se va a probar
      ],
    }).compileComponents(); // Compila los componentes declarados
  });

  // Prueba 1: Verifica que AppComponent se crea correctamente
  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent); // Crea una instancia del componente
    const app = fixture.componentInstance; // Obtiene el componente
    expect(app).toBeTruthy(); // Espera que la instancia sea válida (no null)
  });

  // Prueba 2: Verifica que el valor de la propiedad "title" sea 'ProyectoDAW'
  it(`should have as title 'ProyectoDAW'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('ProyectoDAW'); // Espera que la propiedad title tenga ese valor
  });

  // Prueba 3: Verifica que el título se renderice en el HTML (dentro de una etiqueta <h1>)
  it('should render title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges(); // Aplica bindings y cambios de estado
    const compiled = fixture.nativeElement as HTMLElement; // Obtiene el DOM del componente
    expect(compiled.querySelector('h1')?.textContent).toContain('Hello, ProyectoDAW'); // Verifica el contenido del <h1>
  });
});
