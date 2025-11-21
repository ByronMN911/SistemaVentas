package repositorio;
/*
/**
 * Autor: Byron Melo
 * Fecha: 12/11/2025
 * Versión: 1.0
 * Descripción:
 * Implementación de la interfaz Repository para la entidad Categoria,
 * utilizando Java Database Connectivity (JDBC) para interactuar con la base de datos.
 * Esta clase maneja las operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y las operaciones de activación/desactivación de categorías.
 */


import models.Categoria;
import java.sql.*; // Importa las clases necesarias para trabajar con JDBC
import java.util.List;
import java.util.ArrayList;

public class CategoriaRepositoryJdbcImplement implements Repository<Categoria> {

    // Variable para mantener la conexión a la base de datos
    private Connection conn;

    /**
     * Constructor que recibe una conexión JDBC.
     * @param conn La conexión a la base de datos.
     */
    public CategoriaRepositoryJdbcImplement(Connection conn){
        this.conn = conn;
    }

    /**
     * Método para obtener todas las categorías de la base de datos.
     * @return Una lista de objetos Categoria.
     * @throws SQLException Si ocurre un error al acceder a la BD.
     */
    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        // Usa try-with-resources para asegurar que Statement y ResultSet se cierren automáticamente
        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("select * from categoria")) { // Ejecuta la consulta SQL
            while (rs.next()) { // Itera sobre los resultados
                Categoria categoria = getCategoria(rs); // Mapea la fila actual a un objeto Categoria
                categorias.add(categoria);
            }
        }
        return categorias;
    }

    /**
     * Método para buscar una categoría por su ID.
     * @param id El ID de la categoría a buscar.
     * @return El objeto Categoria si es encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error al acceder a la BD.
     */
    @Override
    public Categoria porId(Long id) throws SQLException {
        Categoria categoria = null;
        // Usa PreparedStatement para evitar inyección SQL (parametrizando el ID)
        try (PreparedStatement stm = conn.prepareStatement("select * from categoria where id = ?")) {
            stm.setLong(1, id); // Establece el valor del primer parámetro (?)
            try (ResultSet rs = stm.executeQuery()) { // Ejecuta la consulta
                if (rs.next()) { // Si encuentra un resultado
                    categoria = getCategoria(rs); // Mapea el resultado a un objeto
                }
            }
        }
        return categoria;
    }

    /**
     * Método para insertar una nueva categoría o actualizar una existente.
     * La lógica de decisión se basa en si el ID de la categoría es válido (diferente de null y > 0).
     * @param categoria El objeto Categoria a guardar o actualizar.
     * @throws SQLException Si ocurre un error al acceder a la BD.
     */
    @Override
    public void guardar(Categoria categoria) throws SQLException {
        String sql;
        // Verifica si es una actualización (si el ID ya existe)
        if(categoria.getId() != null && categoria.getId()>0){
            // Actualizamos una categoría existente
            // La sentencia SQL para UPDATE debe incluir el ID en la cláusula WHERE
            sql = "UPDATE categoria SET nombreCategoria=?, descripcion=?, estado=? WHERE id=?";
        } else {
            // Es una nueva categoría (INSERT)
            // Asume que el 'estado' por defecto para una nueva categoría es 1 (Activo)
            sql = "INSERT INTO categoria (nombreCategoria, descripcion, estado)" +
                    "VALUES (?, ?, 1)";
        }

        try(PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setString(1, categoria.getNombre()); // Asigna el nombre
            stm.setString(2, categoria.getDescripcion()); // Asigna la descripción

            // Lógica para UPDATE: necesita establecer el estado y el ID
            if(categoria.getId() != null && categoria.getId()>0){
                stm.setInt(3, categoria.getEstado()); // Asigna el estado
                stm.setLong(4, categoria.getId()); // Asigna el ID para la cláusula WHERE
            }

            // Ejecuta la sentencia SQL (INSERT o UPDATE)
            stm.executeUpdate();
        }
    }

    /**
     * Metodo para eliminar una categoría por su ID.
     * @param id El ID de la categoría a eliminar.
     * @throws SQLException Si ocurre un error al acceder a la BD.
     */
    @Override
    public void eliminar(Long id) throws SQLException {
        String sql;
        try(PreparedStatement stm = conn.prepareStatement("DELETE FROM categoria WHERE id=?")){
            stm.setLong(1, id); // Asigna el ID
            stm.executeUpdate(); // Ejecuta la eliminación
        }
    }

    /**
     * Metodo auxiliar privado para mapear los datos de un ResultSet a un objeto Categoria.
     * Simplifica el código y asegura la consistencia en la creación de objetos Categoria.
     * @param rs El ResultSet actual.
     * @return Un objeto Categoria poblado con los datos de la fila actual del ResultSet.
     * @throws SQLException Si ocurre un error al leer los datos.
     * */
    private static Categoria getCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("id"));
        categoria.setNombre(rs.getString("nombreCategoria"));
        categoria.setDescripcion(rs.getString("descripcion"));
        categoria.setEstado(rs.getInt("estado"));
        return categoria;
    }

    /**
     * Metodo para cambiar el estado de una categoría a inactivo (estado = 0).
     * @param id El ID de la categoría a desactivar.
     * @throws SQLException Si ocurre un error al acceder a la BD.
     */
    @Override
    public void desactivar(int id) throws SQLException {
        String sql = "UPDATE categoria SET estado = 0 WHERE id=?";
        try(PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setInt(1, id); // Asigna el ID
            stm.executeUpdate(); // Ejecuta la actualización
        }
    }

    /**
     * Metodo para cambiar el estado de una categoría a activo (estado = 1).
     * @param id El ID de la categoría a activar.
     * @throws SQLException Si ocurre un error al acceder a la BD.
     */
    @Override
    public void activar(int id) throws SQLException {
        String sql = "UPDATE categoria SET estado = 1 WHERE id=?";
        try(PreparedStatement stm = conn.prepareStatement(sql)){
            stm.setInt(1, id); // Asigna el ID
            stm.executeUpdate(); // Ejecuta la actualización
        }
    }
}