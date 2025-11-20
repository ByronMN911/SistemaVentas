package services;
import models.Producto;
import java.util.List;
import java.util.Optional;
/*
 * Autor: Byron Melo
 * Fecha: 12/11/2025
 * Versión: 1.0
 * Descripción: Clase especial de tipo interfaz donde definimos 2 metodo que serán
 * implementados por otras clases
 * */

public interface ProductoService {

    //declaramos un metodo que retorna una lista de elementos tipo Producto (sin implementar)
    List<Producto> listar();

    /*Declaramos un metodo que retorna un contenedor que puede o no tener un objeto Producto
     * que se busca por su ID.
     * */
    Optional<Producto> porId(Long id);
}
