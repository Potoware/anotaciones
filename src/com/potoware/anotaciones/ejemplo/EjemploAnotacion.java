package com.potoware.anotaciones.ejemplo;

import com.potoware.anotaciones.ejemplo.models.Producto;
import com.potoware.anotaciones.ejemplo.procesador.JsonSerializador;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;

public class EjemploAnotacion {

    public static void main(String[] args) {
        Producto p = new Producto();
        p.setFecha(LocalDate.now());
        p.setNombre("mesa de centro");
        p.setPrecio(100000l);



        System.out.println("json" + JsonSerializador.convertirJson(p));


    }
}
