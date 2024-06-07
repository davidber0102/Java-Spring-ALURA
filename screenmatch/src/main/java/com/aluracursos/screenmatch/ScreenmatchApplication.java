package com.aluracursos.screenmatch;
import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporada;
import com.aluracursos.screenmatch.principal.EjemploStreams;
import com.aluracursos.screenmatch.principal.Principal;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//EjemploStreams ejemplo = new EjemploStreams();
		//ejemplo.muestraEjemplo();

		Principal principal = new Principal();
		principal.muestraMenu();

		/*System.out.println("Hola Mundo desde Spring");
		var consumo = new ConsumoAPI();
		var json = consumo.obtenerDatos("http://www.omdbapi.com/?t=Game+of+Thrones&apikey=d9c80b09");
		//var json = consumo.obtenerDatos("https://coffee.alexflipnote.dev/random.json");
		System.out.println(json);
		ConvierteDatos conversor = new ConvierteDatos();
		var datos = conversor.obtenerDatos(json, DatosSerie.class);
		System.out.println(datos);
		//--------------------------------------------------
		var json2 = consumo.obtenerDatos("http://www.omdbapi.com/?t=Game+of+Thrones&Season=1&episode=1&apikey=d9c80b09");
		System.out.println(json2);
		ConvierteDatos conversor2 = new ConvierteDatos();
		var datos2 = conversor2.obtenerDatos(json2, DatosEpisodio.class);
		System.out.println(datos2);
		//--------------------------------------------------
		List<DatosTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i <= datos.totTemporadas() ; i++) {
			var json3 = consumo.obtenerDatos("http://www.omdbapi.com/?t=Game+of+Thrones&Season=" + i +"&apikey=d9c80b09");
			var datosTemporadas = conversor.obtenerDatos(json3, DatosTemporada.class);
			temporadas.add(datosTemporadas);
		}
		temporadas.forEach(System.out::println);*/

	}
}

