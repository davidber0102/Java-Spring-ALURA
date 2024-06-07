package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporada;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d9c80b09";
    private final ConvierteDatos conversor = new ConvierteDatos();
    public void muestraMenu(){
        System.out.println("Introduce el nombre de la Serie a buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);
        //busca los datos de todas las temporadas
        List<DatosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= datos.totTemporadas() ; i++) {
            var json2= consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") +"&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json2, DatosTemporada.class);
            temporadas.add(datosTemporadas);
        }
        //temporadas.forEach(System.out::println);

        //MOSTRAR SOLO EL TITULO DE LOS EPISODIOS PARA LAS TEMPORADAS
        /*for (int i = 0; i < datos.totTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporada.size(); j++) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }*/
    temporadas.forEach(t ->t.episodios().forEach(i -> System.out.println(i.titulo())));

    //convertir todos las informaciones a ima lista del tipo de DatpsEpisodios
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                                            .flatMap(t -> t.episodios().stream())
                                            .collect(Collectors.toList());
        //Top 5 episiodios
        /*System.out.println("Top 5 Episodios");
        datosEpisodios.stream()
                .filter(e -> !e.evaluaciones().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primer Filtro sin (N/A) " + e))
                .sorted(Comparator.comparing(DatosEpisodio::evaluaciones).reversed())
                .peek(e -> System.out.println(", Segundo Filtro Ordenacion descendente " + e))
                .map(e -> e.titulo().toUpperCase())
                .peek(e -> System.out.println(", Tercer Filtro MAYUSCULAS " + e))
                .limit(5)
                .peek(e -> System.out.println(", Cuarto Filtro limitado a 5 " + e))
                        .forEach(System.out::println);
*/


        //lista por episodio con nueva clase episoio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());
/*
        episodios.forEach(System.out::println);

        //Busqueda de episodios apartir de x año
        System.out.println("Introduce el año que deas ver de los episodios");
        var fecha = teclado.nextInt();
        //teclado.nextInt();

        LocalDate fechaBusqueda = LocalDate.of(fecha,1, 1 );

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream().filter(e -> e.getFechaDeLanzamaiento() != null
                && e.getFechaDeLanzamaiento().isAfter(fechaBusqueda))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada()
                        + ", Episodio: " + e.getTitulo()
                        + ", Fecha de Lanzmiento " + e.getFechaDeLanzamaiento().format(dtf)
                ));

         */

        //BUSCA EPISODIOS POR PEDADO DEL TITULO
        /*System.out.println("Introduce por favor, el titulo del episodio: ");
        var pedazoTitulo = teclado.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .peek(e -> System.out.println("-  Filtro " + e))
                .findFirst();

        if(episodioBuscado.isPresent()){
            System.out.println("Episodio Encontrado");
            System.out.println("Los datos son: " + episodioBuscado.get());
        }else{
            System.out.println("Episodio No Encontrado");
        }*/

        //
        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion()>0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                            Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion()>0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("-----Media de las evaluaciones; " + est.getAverage() +
                            ", \n Episodio Mejor Evaluado:" + est.getMax() +
                            ", \n Episodio Peor Evaluado:" + est.getMin());

    }
}
