package repositorio;
/*
 * Autor: Byron Melo
 * Fecha: 12/11/2025
 * Versión: 1.0
 * Descripción: Clase especial de tipo interfaz donde definimos 2 metodo que serán
 * implementados por otras clases
 * */

import models.Categoria;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class CategoriaRepositoryJdbcImplement implements Repository<Categoria> {

    private Connection conn;

    public CategoriaRepositoryJdbcImplement(Connection conn){
        this.conn = conn;
    }

    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        try (Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("select * from categoria")) {
            while (rs.next()) {
                Categoria categoria = getCategoria(rs);
                categorias.add(categoria);
            }
        }
        return categorias;
    }

    @Override
    public Categoria porId(Long id) throws SQLException {
        Categoria categoria = null;
        try (PreparedStatement stm = conn.prepareStatement("select * from categoria where id = ?")) {
            stm.setLong(1, id);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    categoria = getCategoria(rs);
                }
            }
        }
        return categoria;
    }

    @Override
    public void guardar(Categoria categoria) throws SQLException {

    }

    @Override
    public void eliminar(Long id) throws SQLException {

    }

    private static Categoria getCategoria(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setNombre(rs.getString("nombreCategoria"));
        categoria.setDescripcion(rs.getString("descripcion"));
        categoria.setId(rs.getLong("id"));
        return categoria;
    }

    @Override
    public void desactivar(int id) throws SQLException {

    }

    @Override
    public void activar(int id) throws SQLException {

    }
}

