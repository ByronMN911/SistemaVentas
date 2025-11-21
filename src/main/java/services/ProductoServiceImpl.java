package services;
/*
 * Autor: Byron Melo
 * Fecha: 12/11/2025
 * Versión: 1.0
 * Descripción: Clase de servicio que implementa la lógica de negocio para la gestión
 * de productos en la aplicación web. Esta clase actúa como una capa intermedia entre
 * los controladores y el modelo de datos, proporcionando métodos para listar productos
 * y buscar productos específicos por su id.
 * Como aún no manejamos conexiones a bases de datos se simula con datos en memoria.
 * */

//Clase que nos permite importar el objeto de tipo lista
import java.util.List;
//Importamos la clase del modelo de nuestra aplicación web
import models.Producto;
// Se importa la clase Arrays que proporciona métodos útiles para trabajar con arreglos
import java.util.Arrays;
// Se importa Optional para manejar de forma segura valores que pueden o no existir
import java.util.Optional;

public class ProductoServiceImpl {
    //implements ProductoService
    /*Sobreescribimos el metodo heredado de ProductoServices
    @Override
    public List<Producto> listar() {

         * El metodo retorna una lista en forma de arreglo de objetos de tipo Producto
         * Estos objetos que creamos se van al modelo de nuestra aplicación web
         *
        return Arrays.asList(new Producto(1L, "Laptop", "Computación", 256.23),
                new Producto(2L, "Mouse", "Computacion", 25.50),
                new Producto(3L, "Cocina", "Cocina", 25.35));


    }
*/
    /**
     * Se sobrescribe el metodo porId() heredado de la interfaz ProductoService.
     * Este metodo permite buscar un producto específico en el catálogo utilizando
     * su identificador único (ID). Usamos Optional para evitar el NullPointerException.
     *
     * Este metodo es fundamental para operaciones como agregar un producto al carrito,
     * mostrar detalles de un producto específico, o actualizar información.
     *



    @Override
    public Optional<Producto> porId(Long id) {
        /**
         * El proceso se ejecuta en los siguientes pasos:
         *
         * 1. listar() - Se invoca el metodo listar() para obtener la lista completa de productos
         *
         * 2. .stream() - Se convierte la lista en un Stream, que es una secuencia de elementos
         *    sobre la cual se pueden aplicar operaciones funcionales de forma fluida y declarativa.
         *    Los streams permiten procesar colecciones de datos de manera más eficiente y legible.
         *
         * 3. .filter(p -> p.getIdProducto().equals(id)) - Se aplica un filtro al stream:
         *    - p -> representa cada objeto Producto individual de la lista (expresión lambda)
         *    - p.getIdProducto() obtiene el ID del producto actual en la iteración
         *    - .equals(id) compara el ID del producto con el ID recibido como parámetro
         *    - Si la comparación es verdadera, el producto pasa el filtro y continúa en el stream
         *    - Si la comparación es falsa, el producto se descarta del stream
         *    Este filtro actúa como un criterio de selección que solo deja pasar los productos
         *    cuyo ID coincida exactamente con el ID buscado.
         *
         * 4. .findAny() - Metodo terminal que finaliza el stream y busca cualquier elemento
         *    que haya pasado el filtro:
         *    - Si encuentra un producto que cumple la condición, lo envuelve en un Optional
         *      y retorna Optional<Producto> con el producto dentro
         *    - Si NO encuentra ningún producto (la lista está vacía o ninguno coincide),
         *      retorna Optional.empty(), que es un contenedor vacío pero seguro

        return listar().stream().filter(p -> p.getId().equals(id)).findAny();
     */


}

