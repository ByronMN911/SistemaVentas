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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Anotación que mapea este servlet a la URL '/crear'.
@WebServlet("/crear")
public class ProductoFormServlet extends HttpServlet {

    // Maneja las peticiones GET (solicitud inicial del formulario).
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
            IOException {
        // Obtenemos la conexión a la base de datos del contexto de la aplicación.
        // Se asume que la conexión fue guardada previamente por un Listener o filtro.
        Connection conn = (Connection) req.getAttribute("conn");


        // Inicializamos el servicio de productos, pasándole la conexión activa.
        ProductoService service = new ProductoServiceJdbcImpl(conn);
        Long id;

        /**
         * Intentamos obtener el parámetro 'id' de la URL (si es una edición).
         * Si no existe o no es un número válido (primera vez que se accede al formulario para crear),
         * capturamos la excepción y asignamos id = 0L.
         * id > 0 indica que la operación es una edición.
         */
        try {
            // Se intenta parsear el 'id' si viene como parámetro.
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            // Si el parámetro no está o no es válido, se asume que es una creación.
            id = 0L;
        }

        // Inicializamos un objeto Producto vacío.
        Producto producto = new Producto();
        // Inicializamos también su categoría para evitar NullPointerExceptions en el formulario JSP
        // al intentar acceder a producto.getCategoria().getId().
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
        // El Request Dispatcher permite enviar los atributos (categorias, producto) a la vista (JSP).
        getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    // Maneja las peticiones POST (envío del formulario para guardar/actualizar datos).
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtención de la Conexión y Servicio
        // Obtenemos la conexión a la base de datos.
        Connection conn = (Connection) req.getAttribute("conn");
        // Inicializamos el servicio para interactuar con la DB.
        ProductoService service = new ProductoServiceJdbcImpl(conn);

        // 2. Captura de Parámetros del Formulario
        String nombre = req.getParameter("nombre");

        // Captura y parseo del ID de la categoría, manejando errores de formato.
        Long categoriaId;
        try {
            categoriaId = Long.parseLong(req.getParameter("categoria"));
        } catch (NumberFormatException | NullPointerException e) {
            // Si es nulo o no es número, se asigna 0L para la validación.
            categoriaId = 0L;
        }

        // Captura y parseo del stock, manejando errores de formato.
        Integer stock;
        try {
            stock = Integer.valueOf(req.getParameter("stock"));
        } catch (NumberFormatException | NullPointerException e) {
            // Si es nulo o no es número, se asigna 0 para la validación.
            stock = 0;
        }

        // El precio se captura como String inicialmente para manejo de decimales y validación.
        String precioParam = req.getParameter("precio");
        Double precio = null; // Inicializado a null para validación.

        String descripcion = req.getParameter("descripcion");
        String codigo = req.getParameter("codigo");
        // Las fechas también se capturan como String.
        String fecha_elaboracion = req.getParameter("fecha_elaboracion");
        String fecha_caducidad = req.getParameter("fecha_caducidad");

        // 3. Validación de Datos (Almacenamiento de Errores)
        // Se crea un mapa para almacenar los errores de validación.
        Map<String, String> errores = new HashMap<>();

        // Validación: Nombre no puede ser nulo o vacío.
        if (nombre == null || nombre.isBlank()) {
            errores.put("nombre", "El nombre no puede estar vacío");
        }

        // Validación: Categoría debe ser seleccionada (ID > 0).
        if (categoriaId == 0L) {
            errores.put("categoria", "La categoría no puede estar vacía");
        }

        // Validación: Stock debe ser mayor a 0 (asumiendo que 0 significa que no se ingresó un valor válido).
        if (stock == 0) {
            errores.put("stock", "El stock no puede estar vacío");
        }

        // Validación compleja para Precio.
        if (precioParam == null || precioParam.trim().isEmpty()) {
            errores.put("precio", "El precio no puede estar vacío");
        } else {
            try {
                // Manejo de decimales: reemplaza comas por puntos antes de parsear.
                precioParam = precioParam.trim().replace(",", ".");
                precio = Double.valueOf(precioParam);
                // Validación: Precio debe ser positivo.
                if (precio <= 0) {
                    errores.put("precio", "El precio debe ser mayor que 0");
                }
            } catch (Exception e) {
                // Si falla la conversión a Double.
                errores.put("precio", "El precio es un número inválido");
            }
        }

        // Validación: Código no puede ser nulo o vacío.
        if (codigo == null || codigo.isBlank()) {
            errores.put("codigo", "El código no puede estar vacío");
        }

        // Validación: Fecha de Elaboración no puede ser nula o vacía.
        if (fecha_elaboracion == null || fecha_elaboracion.isBlank()) {
            errores.put("fecha_elaboracion", "La fecha de elaboración no puede estar vacía");
        }

        // Validación: Fecha de Caducidad no puede ser nula o vacía.
        if (fecha_caducidad == null || fecha_caducidad.isBlank()) {
            errores.put("fecha_caducidad", "La fecha de caducidad no puede estar vacía");
        }

        // 4. Conversión de Fechas y ID
        LocalDate fechaElaboracion, fechaCaducidad;
        try {
            // Intenta convertir las fechas de String a LocalDate con el formato esperado.
            fechaElaboracion = LocalDate.parse(fecha_elaboracion, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            fechaCaducidad = LocalDate.parse(fecha_caducidad, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            // Si el formato es incorrecto, se asignan nulos (la validación de campos vacíos ya se hizo).
            fechaElaboracion = null;
            fechaCaducidad = null;
            // Nota: Aquí se podría añadir un error al mapa si se desea validar el formato específico.
        }

        // Captura del ID oculto (si es una edición), o 0L si es nuevo.
        Long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException | NullPointerException e) {
            id = 0L;
        }

        // 5. Creación del Objeto Producto
        // Instanciamos el objeto con los datos capturados y validados.
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);

        // Se crea el objeto Categoria solo con el ID capturado.
        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);

        producto.setCategoria(categoria);
        producto.setStock(stock);
        producto.setDescripcion(descripcion);
        producto.setCodigo(codigo);
        producto.setFechaElaboracion(fechaElaboracion);
        producto.setFechaCaducidad(fechaCaducidad);
        producto.setPrecio(precio);

        // 6. Decisión de Flujo (Éxito vs. Errores)
        // Verificamos si la lista de errores está vacía.
        if (errores.isEmpty()) {
            // Si no hay errores, guardamos/actualizamos el producto en la base de datos.
            service.guardar(producto);
            // Redirigimos a la lista de productos (Post-Redirect-Get Pattern para evitar re-envíos).
            resp.sendRedirect(req.getContextPath() + "/productos");

        } else {
            // Si hay errores, volvemos a mostrar el formulario con los errores y datos.
            // Seteamos el mapa de errores en el request.
            req.setAttribute("errores", errores);
            // Seteamos la lista de categorías (necesaria para llenar el <select> nuevamente).
            req.setAttribute("categorias", service.ListaCategoria());
            // Seteamos el objeto 'producto' completo (con los datos ingresados) para que el formulario JSP
            // mantenga los valores escritos por el usuario.
            req.setAttribute("producto", producto);
            // Reenviamos la petición al formulario JSP para que muestre la vista.
            getServletContext().getRequestDispatcher("/form.jsp").forward(req, resp);
        }
    }
}