// Importa la función para arrancar una app Angular en el navegador
import { platformBrowser } from '@angular/platform-browser';
// Importa el módulo raíz de la aplicación
import { AppModule } from './app/app.module';

// Inicializa la aplicación Angular con el módulo raíz (AppModule)
platformBrowser().bootstrapModule(AppModule, {
  // Optimización opcional para mejorar el rendimiento del cambio de zona
  ngZoneEventCoalescing: true,
})
// Manejo de errores si falla la inicialización
.catch(err => console.error(err));
