package services;

/*
 * Autor: Byron Melo
 * Fecha: 19/11/2025
 * Versión: 1.0
 * Descripción:
 *  Esta clase implementa la interfaz ProductoService y actúa como una capa de
 *  servicio dentro de la arquitectura del sistema. Su función principal es
 *  servir como intermediario entre los controladores y la capa de repositorio.
 *
 *  La clase encapsula la lógica necesaria para manejar excepciones provenientes
 *  del acceso a la base de datos y convertirlas en excepciones de servicio
 *  (ServiceJdbcException), garantizando una separación clara de responsabilidades.
 *
 *  El servicio utiliza un objeto ProductoRepositoryJdbcImplement para ejecutar
 *  las operaciones CRUD y de consulta asociadas a la entidad Producto.
 */

import models.Producto;
import repositorio.ProductoRepositoryJdbcImplement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoServiceJdbcImpl implements ProductoService {

    /**
     * Repositorio encargado de ejecutar las operaciones CRUD
     * directamente en la base de datos mediante JDBC.
     *
     * Este atributo permite que la clase de servicio se mantenga separada
     * de la implementación concreta del acceso a datos.
     */
    private ProductoRepositoryJdbcImplement repositoryJdbc;

    /**
     * Constructor que recibe la conexión a la base de datos.
     * La conexión se utiliza para inicializar la instancia del repositorio.
     * Esto asegura que tanto el servicio como el repositorio compartan la
     * misma conexión dentro del flujo de ejecución.
     *
     * @param connection Conexión activa hacia la base de datos.
     */
    public ProductoServiceJdbcImpl(Connection connection) {
        this.repositoryJdbc = new ProductoRepositoryJdbcImplement(connection);
    }

    /**
     * Obtiene la lista completa de productos registrados.

     * Llama internamente al metodo listar() del repositorio.
     * Envuelve posibles SQLException dentro de una ServiceJdbcException,
     * lo cual permite mantener aisladas las excepciones de base de datos
     * y manejar solo excepciones de servicio en las capas superiores.
     *
     * @return Lista de objetos Producto.

     */
    @Override
    public List<Producto> listar() {
        try {
            return repositoryJdbc.listar();
        } catch (SQLException throwables) {
            throw new ServiceJbdcException(throwables.getMessage(), throwables.getCause());
        }
    }

    /**
     * Busca un producto por su ID y lo devuelve como Optional.
     * Utiliza el metodo porId() del repositorio.
     * Optional permite un manejo más seguro en caso de que el producto no exista.
     * Las excepciones SQL se convierten en excepciones de servicio.
     *
     * @param id Identificador del producto a consultar.
     * @return Optional que contiene el producto si existe o vacío si no se encuentra.

     */
    @Override
    public Optional<Producto> porId(Long id) {
        try {
            return Optional.ofNullable(repositoryJdbc.porId(id));
        } catch (SQLException throwables) {
            throw new ServiceJbdcException(throwables.getMessage(), throwables.getCause());
        }
    }
}
