// Importa las herramientas esenciales para crear un componente en Angular
import { Component } from '@angular/core';
// Para usar directivas como *ngIf, *ngFor en el template
import { CommonModule } from '@angular/common';
// Permite la navegación programática entre rutas
import { Router } from '@angular/router';

@Component({
  selector: 'app-wiki',             // Selector del componente
  standalone: true,                 // Es un componente standalone (independiente)
  imports: [CommonModule],         // Módulos necesarios en el HTML del componente
  templateUrl: './wiki.component.html', // Vista HTML asociada
  styleUrls: ['./wiki.component.css']   // Archivo de estilos CSS
})
export class WikiComponent {

  constructor(private router: Router) {}

  // Lista de enemigos con su nombre, animación, descripción breve y lore (historia)
  enemigos = [
    {
      nombre: 'Ánima de los Caídos',
      gif: 'assets/gif/esqueleto.gif',
      descripcion: 'Restos huecos que vagan sin rumbo, impulsados por un odio que no recuerdan...',
      lore: 'Algunos dicen que fueron guardianes en una era olvidada...'
    },
    {
      nombre: 'Cráneo de Ira Eterna',
      gif: 'assets/gif/calcio.gif',
      descripcion: 'Un fragmento de furia encerrado en una osamenta flotante...',
      lore: 'Se cuenta que surgió tras la traición de un monje...'
    },
    {
      nombre: 'Mariposa del Lamento',
      gif: 'assets/gif/mariposa.gif',
      descripcion: 'Flota en silencio, como una plegaria perdida...',
      lore: 'Dicen que su aleteo es el eco de un último suspiro...'
    },
    {
      nombre: 'Centinela del Silencio',
      gif: 'assets/gif/lanzador.gif',
      descripcion: 'Una silueta quieta en lo alto, que observa. Nunca habla...',
      lore: 'Creado para custodiar secretos enterrados, su puntería nunca falla...'
    }
  ];

  // Método para regresar al dashboard principal
  irADashboard() {
    this.router.navigate(['/dashboard']);
  }
}
