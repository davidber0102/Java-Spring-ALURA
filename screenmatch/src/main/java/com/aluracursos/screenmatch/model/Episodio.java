package com.aluracursos.screenmatch.model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private int temporada;
    private String titulo;
    private int numeroEpisodio;
    private Double evaluacion;
    private LocalDate fechaDeLanzamaiento;

    public Episodio(int numero, DatosEpisodio d) {
        this.temporada = numero;
        this.titulo = d.titulo();;
        this.numeroEpisodio = d.numeroEpisodio();
        try{
            this.evaluacion = Double.valueOf(d.evaluaciones());
        }catch(NumberFormatException e){
            this.evaluacion = 0.0;
        }
        try{
            this.fechaDeLanzamaiento = LocalDate.parse(d.fechaDeLaanzamiento());
        }catch (DateTimeParseException e){
            this.fechaDeLanzamaiento = null;
        }

    }

    public int getTemporada() {
        return temporada;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(int numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public LocalDate getFechaDeLanzamaiento() {
        return fechaDeLanzamaiento;
    }

    public void setFechaDeLanzamaiento(LocalDate fechaDeLanzamaiento) {
        this.fechaDeLanzamaiento = fechaDeLanzamaiento;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", evaluacion=" + evaluacion +
                ", fechaDeLanzamaiento=" + fechaDeLanzamaiento    ;
    }
}
