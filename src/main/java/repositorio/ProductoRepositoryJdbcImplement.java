package repositorio;

/*
 * Autor: Byron Melo
 * Fecha: 19/11/2025
 * Versión: 1.0
 * Descripción:
 *  Esta clase implementa la interfaz Repository para gestionar operaciones
 *  CRUD (Crear, Leer, Actualizar, Eliminar) sobre la tabla "producto" de la
 *  base de datos del sistema de ventas. Utiliza JDBC para ejecutar consultas
 *  SQL y transformar los registros obtenidos en objetos de tipo Producto.
 *
 *  Además, incluye métodos adicionales para activar y desactivar productos,
 *  modificando el campo "condicion" (1=activo, 0=inactivo).
 */

import models.Categoria;
import models.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryJdbcImplement implements Repository<Producto> {

    /**
     * Conexión activa hacia la base de datos.
     * Esta conexión es inyectada mediante el constructor y es utilizada
     * por todos los métodos para ejecutar consultas SQL.
     */
    private Connection conn;

    /**
     * Constructor que recibe una conexión JDBC ya inicializada.
     * Esta conexión permitirá ejecutar todas las operaciones del repositorio.
     */
    public ProductoRepositoryJdbcImplement(Connection conn) {
        this.conn = conn;
    }

    /**
     * Lista todos los productos almacenados en la base de datos.

     * Se ejecuta un SELECT con INNER JOIN para obtener el nombre de la categoría.
     * Cada registro es mapeado a un objeto Producto mediante getProducto().
     * Se devuelve una lista completa ordenada por el ID del producto.
     *
     * @return Lista de productos con su categoría correspondiente.
     * @throws SQLException si existe un problema al ejecutar la consulta.
     */
    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT p.*, c.nombreCategoria AS categoria " +
                             "FROM producto AS p " +
                             "INNER JOIN categoria AS c ON (p.idCategoria = c.id) " +
                             "ORDER BY p.id ASC")) {

            while (rs.next()) {
                Producto p = getProducto(rs);
                productos.add(p);
            }
        }
        return productos;
    }

    /**
     * Busca un producto por su ID.
     * Utiliza PreparedStatement para evitar inyección SQL.
     * Retorna el producto correspondiente o null si no existe.
     * También obtiene el nombre de la categoría mediante un INNER JOIN.
     *
     * @param id Identificador único del producto.
     * @return El objeto Producto si existe, o null si no se encuentra.
     * @throws SQLException si ocurre un error en la consulta.
     */
    @Override
    public Producto porId(Long id) throws SQLException {
        Producto producto = null;
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT p.*, c.nombreCategoria AS categoria " +
                        "FROM producto AS p " +
                        "INNER JOIN categoria AS c ON (p.idCategoria = c.id) " +
                        "WHERE p.id = ?")) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    producto = getProducto(rs);
                }
            }
        }
        return producto;
    }

    /**
     * Guarda un producto en la base de datos (CREATE y UPDATE)
     *  Si el producto tiene ID, se ejecuta un UPDATE.
     *  Si no tiene ID (producto nuevo), se ejecuta un INSERT.
     *  Se registran datos como nombre, categoría, precio, stock, fechas, etc.
     *  El campo "condicion" se coloca en 1 para productos nuevos (activo por defecto).
     *
     * @param producto Objeto Producto que se va a insertar o actualizar.
     * @throws SQLException si ocurre un error en la operación.
     */
    @Override
    public void guardar(Producto producto) throws SQLException {
        String sql;

        if (producto.getId() != null && producto.getId() > 0) {
            // Actualizar producto existente
            sql = "UPDATE producto SET nombreCategoria=?, idCategoria=?, stock=?, precio=?, descripcion=?, codigo=?, " +
                    "fecha_elaboracion=?, fecha_caducidad=? WHERE id=?";
        } else {
            // Insertar nuevo producto
            sql = "INSERT INTO producto (nombreCategoria, idCategoria, stock, precio, descripcion, codigo, fecha_elaboracion, fecha_caducidad, condicion) " +
                    "VALUES (?,?,?,?,?,?,?,?,1)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setLong(2, producto.getCategoria().getId());
            stmt.setInt(3, producto.getStock());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setString(5, producto.getDescripcion());
            stmt.setString(6, producto.getCodigo());

            if (producto.getId() != null && producto.getId() > 0) {
                stmt.setDate(7, Date.valueOf(producto.getFechaElaboracion()));
                stmt.setDate(8, Date.valueOf(producto.getFechaCaducidad()));
                stmt.setLong(9, producto.getId());
            } else {
                stmt.setDate(7, Date.valueOf(producto.getFechaElaboracion()));
                stmt.setDate(8, Date.valueOf(producto.getFechaCaducidad()));
            }
            stmt.executeUpdate();
        }
    }

    /**
     * Elimina un producto de la base de datos (DELETE)
     * Elimina de forma permanente el registro asociado al ID.
     * Utiliza PreparedStatement para seguridad.
     *
     * @param id ID del producto a eliminar.
     * @throws SQLException si existe un error en la operación.
     */
    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Convierte un registro del ResultSet en un objeto Producto.
     * Extrae todos los datos del producto.
     * Crea también la instancia de Categoria asociada.
     * Se usa internamente en listar() y porId().
     *
     * @param rs ResultSet con los datos de un producto.
     * @return Objeto Producto completamente construido.
     * @throws SQLException si no se pueden leer los datos.
     */
    private static Producto getProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombreProducto"));
        p.setStock(rs.getInt("stock"));
        p.setPrecio(rs.getDouble("precio"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setFechaElaboracion(rs.getDate("fecha_elaboracion").toLocalDate());
        p.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
        p.setCondicion(rs.getInt("condicion"));

        Categoria c = new Categoria();
        c.setId(rs.getLong("idCategoria"));
        c.setNombre(rs.getString("categoria"));

        p.setCategoria(c);
        return p;
    }

    /**
     * Desactiva un producto.
     * Cambia el valor del campo "condicion" a 0.
     * El producto no se elimina; solo queda marcado como inactivo.
     *
     * @param id Identificador del producto.
     * @throws SQLException si ocurre un error en la actualización.
     */
    @Override
    public void desactivar(int id) throws SQLException {
        String sql = "UPDATE producto SET condicion = 0 WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Activa un producto previamente desactivado.
     * Cambia el valor del campo "condicion" a 1.
     * Devuelve la disponibilidad del producto sin eliminar información.
     *
     * @param id Identificador del producto.
     * @throws SQLException si ocurre un error en la actualización.
     */
    @Override
    public void activar(int id) throws SQLException {
        String sql = "UPDATE producto SET condicion = 1 WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}
