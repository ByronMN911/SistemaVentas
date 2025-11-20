package controllers;
/*
 * Autor: Byron Melo
 * Fecha: 20/11/2025
 * Versión: 1.0
 * Descripción: Servlet encargado de gestionar la visualización del listado de productos.
 * Recupera la conexión a la base de datos, consulta los productos mediante
 * el servicio correspondiente y genera dinámicamente una página HTML con
 * el listado. Además, valida si el usuario ha iniciado sesión para mostrar
 * información adicional (precio y opciones).
 * */
import jakarta.servlet.ServletException;
import java.io.IOException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

//Importamos las clases de nuestro package services
import services.*;

//Importamos el modelo de nuestra aplicación web
import models.Producto;

//Importamos la clase para crear listas
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/*Definimos 2 llaves para acceder a este servlet
 */
@WebServlet({"/productos.html", "/productos"})
public class ProductoServlet extends HttpServlet {
    //Sobreescribimos el metodo doGet
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Recuperamos la conexión desde el atributo que fue establecido en el filtro ConnectionFilter
        Connection conn = (Connection) req.getAttribute("conn");

        /*Creamos un objeto de tipo ProductosServices que hace referencia a la clase ProductosServicesImplement
         * el cual implementa el metodo, esto es polimorfismo ya que el objeto de tipo ProductoServices en
         * realidad es una instancia de ProductosServicesImplement.
         * Esto se hace así porque en Java es buena práctica programar contra interfaces, no contra clase, por lo
         * tanto, esto se hace siempre que queremos usar una clase que implementa un metodo de una interfaz.
         */
        ProductoService service = new ProductoServiceJdbcImpl(conn);

        //Definimos una lista que usará el metodo listar de nuestro objeto services que ya implementó el metodo
        List<Producto> productos = service.listar();
            /*
            Crear una instancia de la clase LoginServiceSessionImpl que implementa el metodo de la interfaz
            LoginService
             */
        LoginService auth = new LoginServiceSessionImpl();
        Optional<String> usernameOptional = auth.getUsername(req);

            /*Creamos un contenedor que guarda lo que retorna el metodo getUserName, que es el valor
             de nuestra clave username que es un atributo de un objeto HttpSession el cual creamos a partir
             la petición enviada por el cliente
             */

        //Definimos el tipo de contenido que se enviará como respuesta a la petición
        resp.setContentType("text/html;charset=UTF-8");

        /*
         * Usamos un try-with-resources para que el objeto PrintWriter se cierre automáticamente
         * una vez se termine el bloque try
         * */
        try(PrintWriter out = resp.getWriter())
        {
            //Imprimimos un archivo HTML con PrintWriter
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            //Usamos UTF-8 para permitir caracteres especiales como tildes
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Lista de Productos</title>");
            out.println("<link rel=\"stylesheet\" href=\"" + req.getContextPath() + "/css/estilos.css\">");
            out.println("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Lista de Productos</h1>");
            //Mostramos un mensaje si el usuario se logeo correctamente
            if (usernameOptional.isPresent()) {
                out.println("<h3> Hola " + usernameOptional.get() + " Bienvenido!</h3>");
            }
            //Creamos un contenedor div para manejar la posición de la tabla
            out.println("<div>");
            //Creamos una tabla y definimos atributos para darle una presentación más agradable
            out.println("<table> ");
            /*
             * Podríamos utilizar la etiqueta caption para poner un título que vaya unido a la tabla
             * out.println("<caption style= 'text-align: center';>LISTA DE PRODUCTOS</caption>");
             * */

            //Creamos la cabecera de la tabla
            out.println("<thead>");
            //Creamos la fila de la cabecera de la tabla que tendrá un color de fondo azul marino
            out.println("<tr>");
            //Creamos los campos de la cabecera de la tabla que representan los datos de cada producto
            out.println("<th>ID</th>");
            out.println("<th>NOMBRE</th>");
            out.println("<th>STOCK</th>");
            out.println("<th>FECHA PRODUCCIÓN</th>");

            //Se muestra el campo de precio solo si el usuario está logeado
            if(usernameOptional.isPresent()) {
                out.println("<th>PRECIO</th>");
                out.println("<th>OPCIONES</th>");
            }

            out.println("</tr>");
            out.println("</thead>");
            //Cuerpo de la tabla
            out.println("<tbody>");
            /*
             * Creamos un bucle for-each para construir en cada iteración una fila con
             * todos los datos de un producto con base a los atributos definidos en nuestro modelo
             * */
            productos.forEach(p ->{
                //El atributo align='center' nos permite centrar todos los datos de los campos de las filas
                out.println("<tr>");
                //Utilizamos los métodos públicos getters para acceder al valor de cada atributo de un producto
                out.println("<td>" + p.getId() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + p.getStock() + "</td>");
                out.println("<td>" + p.getFechaElaboracion() + "</td>");
                // Si el usuario está logueado mostramos el precio y el enlace para agregar al carrito
                if(usernameOptional.isPresent()) {
                    out.println("<td>" + p.getPrecio()+ "</td>");
                    out.println("<td>");
                    /*
                     * Agregamos un hipervínculo a cada registro de la tabla productos, donde agregamos
                     * un parámetro en la URL que será el ID de cada producto que obtenemos con el metodo
                     * getIdProducto() de nuestra clase Producto
                     * */
                    out.println("<form action='" + req.getContextPath() + "/agregar-carro' method='get' class='form-inline-icon'>");
                    out.println("<input type='hidden' name='id' value='" + p.getId() + "'>");
                    out.println("<button type='submit' class='icon-button' title='Agregar al carrito'>");
                    out.println("<i class='fa fa-shopping-cart' aria-hidden='true'></i>");
                    out.println("</button>");

                    out.println("</form>");
                    out.println("</td>");
                }
                out.println("</tr>");
            });
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("<br>");
            //Hipervínculo para regresar al menú de productos
            out.println("<a href='" + req.getContextPath() + "/index.html'>Regresar</a>");
            out.println("</body>");
            out.println("</html>");

        }
    }
}