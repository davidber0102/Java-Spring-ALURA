package com.alucursos.desafio.Principal;

import com.alucursos.desafio.model.Datos;
import com.alucursos.desafio.model.DatosLibro;
import com.alucursos.desafio.services.ConsumoAPI;
import com.alucursos.desafio.services.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner teclado = new Scanner(System.in);
    public void mestraElMenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);

        var datos =conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        //top 10 libros mas descargados
        System.out.println("Top libros mas descargados");
        datos.resultadolibros().stream()
                .sorted(Comparator.comparing(DatosLibro::numeroDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
//Busqueda de {libros por nombre
        System.out.println("Ingrese el nombre del libro que deseas buscar");
        var tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));

        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultadolibros()
                .stream().filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if(libroBuscado.isPresent()){
            System.out.println("Libro Encontrado");
            System.out.println(libroBuscado.get());
        }else {
            System.out.println("Libro No Encontrado");
        }
//Trabajando con Estadisticas
        DoubleSummaryStatistics est = datos.resultadolibros().stream()
                .filter(d -> d.numeroDescargas() > 0)
                .collect(Collectors.summarizingDouble(DatosLibro::numeroDescargas));

        System.out.println("Cantidad media de descargas: " + est.getAverage() +
                "\n Cantidad maxima de descargas: " + est.getMax() +
                "\n Cantidad Minima de Descargas: " + est.getMin() +
                "\n Cantidad de registros evaluados para calcular las estadisticas: " + est.getCount() );

    }
}
