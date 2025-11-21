package services;

/*
 * Autor: Byron Melo
 * Fecha: 19/11/2025
 * Versión: 1.0
 * Descripción:
 * Esta clase implementa la interfaz ProductoService y actúa como una capa de
 * servicio dentro de la arquitectura del sistema. Su función principal es
 * servir como intermediario entre los controladores y la capa de repositorio.
 *
 * La clase encapsula la lógica necesaria para manejar excepciones provenientes
 * del acceso a la base de datos y convertirlas en excepciones de servicio
 * (ServiceJdbcException), garantizando una separación clara de responsabilidades.
 *
 * El servicio utiliza un objeto ProductoRepositoryJdbcImplement para ejecutar
 * las operaciones CRUD y de consulta asociadas a la entidad Producto.
 */

import models.Categoria;
import models.Producto;
import repositorio.CategoriaRepositoryJdbcImplement;
import repositorio.ProductoRepositoryJdbcImplement;
import repositorio.Repository;

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
    private Repository<Producto> repositoryJdbc;
    private Repository<Categoria> repositoryCategoriaJdbc;

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
        this.repositoryCategoriaJdbc = new CategoriaRepositoryJdbcImplement(connection);
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
            // Convierte la excepción SQL a una excepción de servicio.
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
            // Convierte la excepción SQL a una excepción de servicio.
            throw new ServiceJbdcException(throwables.getMessage(), throwables.getCause());
        }
    }

    /**
     * Persiste un objeto Producto en la base de datos.
     * Si el producto tiene un ID, se realiza una **actualización (UPDATE)**;
     * si no tiene ID, se realiza una **inserción (INSERT)**.
     * La lógica de actualización/inserción reside en el repositorio.
     *
     * @param producto El objeto Producto a guardar o actualizar.
     * @throws ServiceJbdcException Si ocurre un error durante la operación de persistencia.
     */
    @Override
    public void guardar(Producto producto) {
        try{
            repositoryJdbc.guardar(producto);
        } catch (SQLException throwables) {
            // Convierte la excepción SQL a una excepción de servicio.
            throw new ServiceJbdcException(throwables.getMessage(), throwables.getCause());
        }
    }

    /**
     * Elimina un producto de la base de datos utilizando su ID.
     * Llama al metodo eliminar() del repositorio.
     *
     * @param id Identificador del producto a eliminar.
     * @throws ServiceJbdcException Si ocurre un error al intentar eliminar el producto.
     */
    @Override
    public void eliminar(Long id) {
        try{
            repositoryJdbc.eliminar(id);
        }catch (SQLException throwables){
            // Convierte la excepción SQL a una excepción de servicio.
            throw new ServiceJbdcException(throwables.getMessage(), throwables.getCause());
        }
    }

    /**
     * Obtiene la lista completa de todas las categorías disponibles.
     * Utiliza el repositorio de Categoría para acceder a la base de datos.
     *
     * @return Lista de objetos Categoria.
     * @throws ServiceJbdcException Si ocurre un error al obtener las categorías.
     */
    @Override
    public List<Categoria> ListaCategoria() {
        try{
            return repositoryCategoriaJdbc.listar();
        }catch (SQLException throwables){
            // Convierte la excepción SQL a una excepción de servicio.
            throw new ServiceJbdcException(throwables.getMessage(), throwables.getCause());
        }
    }

    /**
     * Busca una categoría por su ID y la devuelve como Optional.
     * Permite obtener una categoría específica para ser utilizada, por ejemplo,
     * al crear o actualizar un Producto.
     *
     * @param id Identificador de la categoría a consultar.
     * @return Optional que contiene la Categoría si existe o vacío si no se encuentra.
     * @throws ServiceJbdcException Si ocurre un error al consultar la categoría.
     */
    @Override
    public Optional<Categoria> porIdCategoria(Long id) {
        try{
            return Optional.ofNullable(repositoryCategoriaJdbc.porId(id));
        }catch(SQLException throwables){
            // Convierte la excepción SQL a una excepción de servicio.
            throw new ServiceJbdcException(throwables.getMessage(), throwables.getCause());
        }
    }
}