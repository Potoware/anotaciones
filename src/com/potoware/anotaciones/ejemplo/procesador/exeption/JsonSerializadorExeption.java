package com.potoware.anotaciones.ejemplo.procesador.exeption;

public class JsonSerializadorExeption extends RuntimeException{
    public JsonSerializadorExeption(String message) {
        super(message);
    }
}
