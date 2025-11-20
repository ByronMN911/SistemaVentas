package controllers;
/*
 * Autor: Byron Melo
 * Fecha: 19/11/2025
 * Versión: 1.0
 * Descripción: Servlet encargado de agregar un producto al carrito de compras.
 * Obtiene el producto por su ID, lo encapsula en un ItemCarro y lo añade
 * al objeto DetalleCarro almacenado en la sesión del usuario.
 * Finalmente redirige a la vista donde se muestra el contenido del carrito.
 * */

// Se importan las clases necesarias del API de Servlets de Jakarta EE para manejar peticiones web
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Se importan las clases del modelo que representan la estructura de datos del carrito
import models.DetalleCarro;
import models.ItemCarro;
import models.Producto;

// Se importan las clases de servicio que contienen la lógica de negocio
import services.ProductoService;
import services.ProductoServiceImpl;

// Se importa la clase para manejar excepciones de entrada/salida
import java.io.IOException;
// Se importa Optional para manejar valores que pueden o no existir
import java.sql.Connection;
import java.util.Optional;

//Anotación para acceder al servlet
@WebServlet("/agregar-carro")
/**
 * Clase servlet que maneja la funcionalidad de agregar productos al carrito de compras.
 * Esta clase extiende HttpServlet, lo que le permite procesar peticiones HTTP.
 * Es el controlador responsable de recibir solicitudes del usuario para agregar
 * productos al carrito, validar que el producto existe, y actualizar la sesión
 * con el carrito modificado.
 */
public class AgregarCarroServlet extends HttpServlet {

    /**
     * Se sobrescribe el metodo doGet() que maneja las peticiones HTTP GET.
     * Este metodo se ejecuta automáticamente cuando un usuario accede a la URL
     * "/agregar-carro" mediante una petición GET haciendo click en un enlace
     * */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /**
         * Se extrae el parámetro "id" de la URL de la petición.
         * Este ID corresponde al identificador único del producto que el usuario
         * desea agregar al carrito y es fundamental para buscar el producto específico
         * en el catálogo.
         */
        Long id = Long.parseLong(req.getParameter("id"));

        // Obtenemos la conexión desde el filtro ConnectionFilter
        Connection conn = (Connection) req.getAttribute("conn");

        /**
         * Se crea un objeto de tipo ProductoServiceImpl que implementa la interfaz
         * ProductoService, de esta manera accedemos a los métodos del servicio
         * como listar() y porId() para interactuar con el catálogo de productos.
         */
        ProductoService service = new ProductoServiceImpl();

        /**
         * Se busca el producto específico por su ID utilizando el servicio.
         * El metodo porId() retorna un Optional<Producto> que puede contener el
         * producto si existe en el catálogo (Optional con valor) o un Optional
         * vacío si no se encuentra el producto
         *
         * El uso de Optional es una práctica segura que obliga a verificar la
         * existencia del producto antes de usarlo, evitando NullPointerException.
         */
        Optional<Producto> producto = service.porId(id);

        /**
         * Se verifica si el producto existe en el catálogo usando isPresent().
         * Este metodo retorna true si el Optional contiene un producto, false si está vacío.
         * Solo se procede a agregar el producto al carrito si efectivamente existe.
         * Esto previene errores al intentar agregar productos inexistentes.
         */
        if (producto.isPresent()) {
            /**
             * Se crea un nuevo objeto ItemCarro con el producto encontrado.
             * El constructor recibe dos parámetros, la cantidad y un objeto Producto
             * que lo extraemos del Optional con producto.get()
             * Este nuevo ItemCarro representa el producto que se agregará al carrito.
             */
            ItemCarro item = new ItemCarro(1, producto.get());

            /**
             * Se obtiene la sesión HTTP del usuario actual.
             * La sesión es un mecanismo del servidor para mantener información
             * específica de cada usuario entre múltiples peticiones HTTP.
             * req.getSession() obtiene la sesión existente o crea una nueva si no existe.
             *
             * La sesión es crucial para mantener el estado del carrito de compras,
             * ya que HTTP es un protocolo sin estado (stateless) y necesitamos
             * recordar qué productos ha agregado cada usuario.
             */
            HttpSession session = req.getSession();

            /**
             * Se declara una variable para almacenar el objeto DetalleCarro.
             * Esta variable contendrá el carrito de compras del usuario, ya sea
             * recuperándolo de la sesión si existe, o creando uno nuevo si es
             * la primera vez que el usuario agrega un producto.
             */
            DetalleCarro detalleCarro;

            /**
             * Se verifica si existe un carrito en la sesión del usuario.
             * session.getAttribute("carro") intenta obtener el objeto guardado
             * con la clave "carro" en la sesión, si retorna null, significa que
             * el usuario aún no tiene un carrito creado porque es su primera compra
             */
            if (session.getAttribute("carro") == null) {
                /**
                 * Si no existe un carrito en la sesión, se crea uno nuevo.
                 * Instanciamos la variable detalleCarro que declaramos anteriormente.
                 */
                detalleCarro = new DetalleCarro();

                /**
                 * Se guarda el nuevo carrito en la sesión con la clave "carro".
                 * Esto permite que el carrito persista entre diferentes peticiones
                 * del mismo usuario, por lo tanto, la sesión mantiene este objeto en memoria del
                 * servidor hasta que expire o el usuario cierre su sesión.
                 */
                session.setAttribute("carro", detalleCarro);
            } else {
                /**
                 * Si ya existe un carrito en la sesión, se recupera.
                 * Se hace un cast de Object a DetalleCarro porque session.getAttribute()
                 * retorna un tipo genérico Object. Este cast es seguro porque nosotros
                 * controlamos qué tipo de objeto guardamos con la clave "carro".
                 */
                detalleCarro = (DetalleCarro) session.getAttribute("carro");
            }

            /**
             * Se agrega el nuevo ítem al carrito utilizando el metodo addItemCarro().
             * Este metodo verifica si el producto ya existe en el carrito, si existe
             * incrementa la cantidad en 1 unidad y si no existe lo agrega como un nuevo ítem
             *
             * Después de esta operación, el carrito en la sesión queda actualizado
             * automáticamente porque detalleCarro es una referencia al mismo objeto
             * que está almacenado en la sesión.
             */
            detalleCarro.addItemCarro(item);
        }

        /**
         * Se redirige al usuario a la página de visualización del carrito.
         *
         * resp.sendRedirect() envía una respuesta HTTP 302 (redirección temporal)
         * al navegador, indicándole que debe hacer una nueva petición a otra URL.
         *
         * La URL de redirección se construye concatenando req.getContextPath() que
         * obtiene la ruta raíz de la aplicación web y "/ver-carro" que es la ruta
         * específica del servlet que muestra el carrito
         * De esta manera mejoramos la experiencia del usuario mostrándole inmediatamente su carrito,
         * evitando que el usuario pueda recargar la página y agregar el mismo producto
         *   múltiples veces y siguiendo el patrón Post-Redirect-Get
         */
        resp.sendRedirect(req.getContextPath() + "/ver-carro");
    }
}