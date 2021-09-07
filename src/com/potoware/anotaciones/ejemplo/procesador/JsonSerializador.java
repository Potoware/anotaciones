package com.potoware.anotaciones.ejemplo.procesador;

import com.potoware.anotaciones.ejemplo.Init;
import com.potoware.anotaciones.ejemplo.JsonAtributo;
import com.potoware.anotaciones.ejemplo.procesador.exeption.JsonSerializadorExeption;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class JsonSerializador {
    public static void inicializarObjeto(Object objeto){
        if(Objects.isNull(objeto)){
            throw new JsonSerializadorExeption("El objeto no puede ser null");
        }

        Method[] metodos = objeto.getClass().getDeclaredMethods();
        Arrays.stream(metodos)
                .filter(m->m.isAnnotationPresent(Init.class))
                .forEach(m-> {
                    try {
                        m.setAccessible(true);
                        m.invoke(objeto);
                    } catch (IllegalAccessException e) {
                       throw new JsonSerializadorExeption("No se puede incializar");
                    } catch (InvocationTargetException e) {
                        throw new JsonSerializadorExeption("No se puede incializar");
                    }
                });
    }

    public static String convertirJson(Object object){

        if(Objects.isNull(object)){
            throw new JsonSerializadorExeption("El objeto no puede ser null");
        }
        inicializarObjeto(object);
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
                           nuevoValor= Arrays.stream(nuevoValor.split(" "))
                                   .map(palabra-> palabra.substring(0,1).toUpperCase() + palabra.substring(1).toLowerCase()).collect(Collectors.joining(" "));

                         /*   String nuevoValor = (String) valor;
                            nuevoValor = nuevoValor.substring(0,1).toUpperCase()+
                                    nuevoValor.substring(1).toLowerCase();*/
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
