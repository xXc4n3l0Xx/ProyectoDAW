// Importa decorador para definir un módulo de Angular
import { NgModule } from '@angular/core';
// Necesario para que la aplicación funcione en navegadores
import { BrowserModule } from '@angular/platform-browser';
// Permite trabajar con formularios basados en templates
import { FormsModule } from '@angular/forms';
// Permite realizar peticiones HTTP
import { HttpClientModule } from '@angular/common/http';

// Componente raíz de la aplicación
import { AppComponent } from './app.component';
// Componente de la vista principal (home/login)
import { HomeComponent } from './home/home.component';
// Módulo que contiene la configuración de rutas
import { AppRoutingModule } from './app-routing.module';

@NgModule({
  declarations: [
    AppComponent // Declara el componente raíz (no es standalone)
  ],
  imports: [
    BrowserModule,     // Requerido para ejecutar la app en un navegador
    FormsModule,       // Para manejar formularios
    HttpClientModule,  // Para peticiones HTTP
    AppRoutingModule,  // Importa las rutas ya configuradas
    HomeComponent      // Componente standalone importado directamente
  ],
  providers: [],       // Servicios globales (vacío en este caso)
  bootstrap: [AppComponent] // Componente que arranca la app
})
export class AppModule {}
