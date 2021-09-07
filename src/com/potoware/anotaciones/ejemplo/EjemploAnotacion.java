package com.potoware.anotaciones.ejemplo;

import com.potoware.anotaciones.ejemplo.models.Producto;

import java.time.LocalDate;

public class EjemploAnotacion {

    public static void main(String[] args) {
        Producto p = new Producto();
        p.setFecha(LocalDate.now());
        p.setNombre("Mesa de centro");
        p.setPrecio(100000l);
    }
}
