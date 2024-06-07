package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repositorio.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d9c80b09";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSerie = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar Series por Titulo 
                    5 - Top 5 Mejores Series  
                    6 - Buscar Series por Categoria 
                    7 - Filtrar series
                    8 - Buscar episodios por Titulo  
                    9 - Top 5 de Episodios por Serie  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSerieBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();

                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        //DatosSerie datosSerie = getDatosSerie();
        mostrarSerieBuscadas();
        System.out.println("Escriba el nombre el nombre de la serie y que episodios deseas ver: ");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                                    .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                                        .findFirst();
        if (serie.isPresent()) {
            var serieEmcontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEmcontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEmcontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodos = temporadas.stream()
                    .flatMap(d ->d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEmcontrada.setEpisodios(episodos);
            repositorio.save(serieEmcontrada);
        }
    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        //datosSerie.add(datos);
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        System.out.println(datos);
    }

    private void mostrarSerieBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escriba el nombre el nombre de la serie deseas ver: ");
        var nombreSerie = teclado.nextLine();

        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("La Serie buscada es: " + serieBuscada.get());
        }else{
            System.out.println("Serie No Ecnontrada");
        }
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Titulo: " + s.getTitulo()
                + " Evaluacion: " + s.getEvaluacion() ));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Escriba el gnero/categoria de la serie a buscar: ");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);

        System.out.println("Las Series de la Categoria: " + genero);
        seriePorCategoria.forEach(System.out::println);
    }

    public void filtrarSeriesPorTemporadaYEvaluacion(){
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Com evaluación apartir de cuál valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }

    private void buscarEpisodiosPorTitulo() {
        System.out.println("Escribe el nombre del episodio que deseas buscar");
        var nombreEpisodio = teclado.nextLine();

        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf("Serie: %s Temporada %s Episodio %s Evaluacion %s",
                e.getSerie(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));
    }

    private void buscarTop5Episodios() {
        buscarSeriesPorTitulo();

        if (serieBuscada.isPresent()){
            Serie serrie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serrie);
            topEpisodios.forEach(e -> System.out.printf("Serie: %s - Temporada %s - Episodio %s - Evaluacion %s",
                    e.getSerie(), e.getTemporada(), e.getTitulo(), e.getEvaluacion()));

        }
    }
}

