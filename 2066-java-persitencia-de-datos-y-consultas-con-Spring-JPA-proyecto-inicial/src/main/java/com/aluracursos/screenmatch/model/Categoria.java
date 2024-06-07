package com.aluracursos.screenmatch.model;

public enum Categoria {
    ACCION("Action", "Accion"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    DRAMA("Drama", "Drama"),
    CRIMEN("Crime", "Crimen");

    private String categoriaOmdb;
    private String cateoriaEspanol;

    Categoria (String categoriaOmdb, String cateoriaEspanol) {
        this.categoriaOmdb = categoriaOmdb;
        this.cateoriaEspanol = cateoriaEspanol;
    }

    public static Categoria fromString(String text){
        for(Categoria c : Categoria.values()){
            if(c.categoriaOmdb.equalsIgnoreCase(text)){
                return c;
            }
        }
        throw new IllegalArgumentException("Minguna Cateogira encontrada: " + text);
    }

    public static Categoria fromEspanol(String text){
        for(Categoria c : Categoria.values()){
            if(c.cateoriaEspanol.equalsIgnoreCase(text)){
                return c;
            }
        }
        throw new IllegalArgumentException("Minguna Cateogira encontrada: " + text);
    }
}
