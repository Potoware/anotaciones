package com.potoware.anotaciones.ejemplo.procesador;

import com.potoware.anotaciones.ejemplo.JsonAtributo;
import com.potoware.anotaciones.ejemplo.procesador.exeption.JsonSerializadorExeption;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class JsonSerializador {

    public static String convertirJson(Object object){

        if(Objects.isNull(object)){
            throw new JsonSerializadorExeption("El objeto no puede ser null");
        }

        Field[] atributos = object.getClass().getDeclaredFields();
        return Arrays.stream(atributos)
                .filter(f -> f.isAnnotationPresent(JsonAtributo.class))
                .map(f -> {
                    f.setAccessible(true);
                    String nombre = f.getAnnotation(JsonAtributo.class).nombre().equals("")
                            ? f.getName()
                            :f.getAnnotation(JsonAtributo.class).nombre();
                    try {
                        Object valor = f.get(object);
                        if(f.getAnnotation(JsonAtributo.class).capitalizar() &&
                        valor instanceof String){
                            String nuevoValor = (String) valor;
                            nuevoValor = nuevoValor.substring(0,1).toUpperCase()+
                                    nuevoValor.substring(1).toLowerCase();
                            f.set(object,nuevoValor);
                        }
                        return "\"" + nombre + "\":\"" + f.get(object)+ "\"";
                    } catch (IllegalAccessException e) {
                        throw new JsonSerializadorExeption("Error al serializar a json: "+e.getMessage());

                    }


                })
                .reduce("{",(a,b) ->{
                    if("{".equals(a)){
                        return a+b;
                    }
                    else{
                        return a +", "+b;
                    }
                }).concat("}");
    }
}
