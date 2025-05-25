import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-wiki',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wiki.component.html',
  styleUrls: ['./wiki.component.css']
})

export class WikiComponent {

  constructor(private router: Router) {}

  enemigos = [
    {
      nombre: 'Ánima de los Caídos',
      gif: 'assets/gif/esqueleto.gif',
      descripcion: 'Restos huecos que vagan sin rumbo, impulsados por un odio que no recuerdan. Les mueve una voluntad rota, apenas un eco de lo que fueron.',
      lore: 'Algunos dicen que fueron guardianes en una era olvidada. Siguen atacando lo que aún respira, no por deber, sino por el peso de su maldición. Aquellos que mueren en su presencia, lo hacen sin ser vistos, como si el mundo ya los hubiera olvidado.'
    },
    {
      nombre: 'Cráneo de Ira Eterna',
      gif: 'assets/gif/calcio.gif',
      descripcion: 'Un fragmento de furia encerrado en una osamenta flotante. Sus ojos ya no miran, pero aún buscan aquello que les fue arrebatado.',
      lore: 'Se cuenta que surgió tras la traición de un monje, cuya cabeza fue maldecida para cazar sin descanso. Su grito silencioso precede al dolor, y su aparición es el último aviso para quienes se atreven a recordar lo prohibido.'
    },
    {
      nombre: 'Mariposa del Lamento',
      gif: 'assets/gif/mariposa.gif',
      descripcion: 'Flota en silencio, como una plegaria perdida entre ruinas que nadie osa cruzar. Su luz no guía, sólo atrae a los que ya han renunciado.',
      lore: 'Dicen que su aleteo es el eco de un último suspiro, atrapado para siempre en la neblina. Algunos aseguran haber visto lágrimas caer de sus alas... aunque nadie regresa para contarlo con certeza.'
    },
    {
      nombre: 'Centinela del Silencio',
      gif: 'assets/gif/lanzador.gif',
      descripcion: 'Una silueta quieta en lo alto, que observa. Nunca habla. Nunca duda. Nunca falla.',
      lore: 'Creado para custodiar secretos enterrados, su puntería nunca falla... y nunca perdona. Los pocos que han escapado de su mirada, dicen que el verdadero castigo no es morir, sino ser visto.'
    }
  ];

  irADashboard() {
    this.router.navigate(['/dashboard']);
  }
}

