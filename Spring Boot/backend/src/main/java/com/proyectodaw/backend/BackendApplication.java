package com.proyectodaw.backend;

// Importa el lanzador de aplicaciones Spring Boot
import org.springframework.boot.SpringApplication;
// Importa la anotación que habilita la configuración automática y escaneo de componentes
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Marca esta clase como la aplicación principal de Spring Boot
public class BackendApplication {

	// Método principal que inicia la aplicación
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args); // Arranca el servidor y carga el contexto de Spring
	}
}
