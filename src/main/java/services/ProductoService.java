package services;
import models.Categoria;
import models.Producto;
import java.util.List;
import java.util.Optional;
/*
 * Autor: Byron Melo
 * Fecha: 12/11/2025
 * Versión: 1.0
 * Descripción: Clase especial de tipo interfaz donde definimos metodos que serán
 * implementados por las clases de servicio de nuestra aplicación.
 * Las clases de servicio actúan como un intermediario entre los servlets y nuestras clases DAOS,
 * o sea las clases de nuestro package repositorio que utilizan JDBC para conectarse a la base de datos y
 * realizar operaciones CRUD.
 * */

public interface ProductoService {

    //declaramos un metodo que retorna una lista de elementos tipo Producto (sin implementar)
    List<Producto> listar();

    /*Declaramos un metodo que retorna un contenedor que puede o no tener un objeto Producto
     * que se busca por su ID.
     * */
    Optional<Producto> porId(Long id);

    void guardar(Producto producto);
    void eliminar(Long id);

    //Mostrar una lista de categorias
    List<Categoria> ListaCategoria();

    //Obtenemos una categoria por su id
    Optional<Categoria> porIdCategoria(Long id);

}
