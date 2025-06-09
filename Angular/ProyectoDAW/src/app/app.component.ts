// Importa el decorador Component para definir un componente de Angular
import { Component } from '@angular/core';

@Component({
  selector: 'app-root', // Nombre del selector que se usará en index.html (punto de entrada)
  templateUrl: './app.component.html', // Ruta al archivo HTML asociado (vista del componente)
  standalone: false, // Este componente no es standalone, forma parte de un módulo
  styleUrl: './app.component.css' // Ruta al archivo de estilos CSS del componente
})
export class AppComponent {
  title = 'ProyectoDAW'; // Título de la aplicación, usado en el template y pruebas
}
