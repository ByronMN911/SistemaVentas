package controllers;
/*
/**
 * Autor: Byron Melo
 * Fecha: 19/11/2025
 * Versión: 1.0
 * Descripción:
 * Este Servlet maneja la lógica para mostrar el formulario de creación o edición de un producto.
 * Sirve para dos propósitos principales:
 * 1. Crear un nuevo producto (cuando no se recibe un id).
 * 2. Editar un producto existente (cuando se recibe un id válido).
 *
 * Obtiene el producto a editar (si aplica) y la lista de categorías,
 * y luego redirige la solicitud a la página JSP del formulario.
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Categoria;
import models.Producto;
import services.ProductoService;
import services.ProductoServiceJdbcImpl;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

@WebServlet("/producto/form")
public class ProductoFormServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        // Obtenemos la conexión a la base de datos del contexto de la aplicación.
        // Se asume que la conexión fue guardada previamente por un Listener o filtro.
        Connection conn = (Connection) req.getServletContext().getAttribute("conn");

        // Inicializamos el servicio de productos, pasándole la conexión activa.
        ProductoService service = new ProductoServiceJdbcImpl(conn);
        Long id;

        /**
         * Intentamos obtener el parámetro 'id' de la URL.
         * Si no existe o no es un número válido (primera vez que se accede al formulario para crear),
         * capturamos la excepción y asignamos id = 0L.
         * id > 0 indica que la operación es una edición.
         */
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            id = 0L;
        }

        // Inicializamos un objeto Producto vacío.
        Producto producto = new Producto();
        // Inicializamos también su categoría para evitar NullPointerExceptions en el formulario JSP.
        producto.setCategoria(new Categoria());

        // Verificamos si el ID es mayor a 0, indicando una solicitud de edición.
        if (id > 0) {
            // Buscamos el producto por ID usando el servicio.
            Optional<Producto> o = service.porId(id);
            // Si el producto existe en la base de datos (Optional.isPresent()),
            // lo asignamos a la variable 'producto' para precargar el formulario.
            if (o.isPresent()) {
                producto = o.get();
            }
        }

        // Guardamos la lista completa de categorías en el request para que el JSP pueda llenar el <select>
        req.setAttribute("categorias", service.ListaCategoria());
        // Guardamos el objeto producto (vacío o cargado con datos para edición) en el request.
        req.setAttribute("producto", producto);

        // Redirigimos la solicitud al JSP del formulario.
        // El Request Dispatcher permite enviar los atributos del request a la vista (JSP).
        getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
    }
}