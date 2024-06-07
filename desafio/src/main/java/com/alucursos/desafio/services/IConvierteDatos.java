package com.alucursos.desafio.services;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}