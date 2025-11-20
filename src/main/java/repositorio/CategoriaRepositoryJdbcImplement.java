package repositorio;


import models.Categoria;

import java.sql.SQLException;
import java.util.List;

public class CategoriaRepositoryJdbcImplement implements Repository<Categoria>{

    @Override
    public List<Categoria> listar() throws SQLException {
        return List.of();
    }

    @Override
    public Categoria porId(Long id) throws SQLException {
        return null;
    }

    @Override
    public void guardar(Categoria categoria) throws SQLException {

    }

    @Override
    public void eliminar(Long id) throws SQLException {

    }

    @Override
    public void activar(int id) throws SQLException {

    }

    @Override
    public void desactivar(int id) throws SQLException {

    }
}

