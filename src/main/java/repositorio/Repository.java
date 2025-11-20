package repositorio;

/*
 * Autor: Byron Melo
 * Fecha: 19/11/2025
 * Versión: 1.1
 * Descripción:
 *  Esta interfaz genérica define el contrato base para un repositorio que
 *  interactúa con la base de datos. Proporciona los métodos CRUD esenciales
 *  (crear, leer, actualizar y eliminar) y, adicionalmente, métodos para
 *  activar y desactivar un producto según su ID. La interfaz utiliza un
 *  tipo genérico <T> para permitir su uso con diferentes entidades.
 */

import java.sql.SQLException;
import java.util.List;

public interface Repository<T> {

    /**
     * Obtiene la lista completa de registros de la entidad.
     * @return Lista de objetos del tipo T almacenados en la base de datos.
     * @throws SQLException Si ocurre un error al ejecutar la consulta.
     */
    List<T> listar() throws SQLException;

    /**
     * Busca un registro por su identificador único.
     * @param id Identificador del registro.
     * @return El objeto encontrado o null si no existe.
     * @throws SQLException Si ocurre un error en la consulta.
     */
    T porId(Long id) throws SQLException;

    /**
     * Guarda un objeto en la base de datos.
     * Si el objeto no existe, lo inserta; si existe, lo actualiza.
     * @param t Objeto a guardar.
     * @throws SQLException Si ocurre un error al insertar o actualizar.
     */
    void guardar(T t) throws SQLException;

    /**
     * Elimina permanentemente un registro de la base de datos según su ID.
     * @param id Identificador del registro a eliminar.
     * @throws SQLException Si ocurre un error al eliminar.
     */
    void eliminar(Long id) throws SQLException;

    /**
     * Desactiva un producto en la base de datos.
     * Normalmente implica cambiar un campo booleano o estado (activo = false).
     * @param id Identificador del producto a desactivar.
     * @throws SQLException Si ocurre un error durante la actualización.
     */
    void desactivar(int id) throws SQLException;

    /**
     * Activa un producto en la base de datos.
     * Cambia el estado del producto para que esté disponible nuevamente.
     * @param id Identificador del producto a activar.
     * @throws SQLException Si ocurre un error durante la actualización.
     */
    void activar(int id) throws SQLException;
}
