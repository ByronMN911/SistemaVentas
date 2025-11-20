package controllers;
/*
 * Autor: Byron Melo
 * Fecha: 11/11/2025
 * Versión: 1.0
 * Descripción: Esta clase hereda de HttpServlet para manejar peticiones get y post mediante
 * las llaves login y login.html.
 * El metodo doGet se encarga de verificar si existe una sesion activa, si existe te muestra un mensaje
 * de bienvenida y te indica el número de veces que has iniciado sesión correctamente.
 * Si no existe una sesión activa te redirige al formulario, es decir, a login.jsp
 * El metodo doPost se encarga de capturas los parámetros que se envían a través del formulario usando
 * la llave login y el metodo POST, a sí mismo, si el usuario ingresa las credenciales correctas
 * se crea un objeto de tipo HttpSession, es decir, una sesión.
 * */
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import services.LoginService;
import services.LoginServiceSessionImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

@WebServlet({"/login", "/login.html"})
public class LoginServlet extends HttpServlet {
    /*Inicializamos las variables estáticas para el login,
     *El valor de estas variables nunca cambiará porque son constantes, por ello,
     * se declaran afuera y antes de todos los métodos
     * para usarlo en cualquier parte de nuestro programa.
     * */
    final static String USERNAME = "admin";
    final static String PASSWORD = "123";

    /*
    Definimos una variable estática de tipo int que actúa guarda el número de veces
    que el usuario ha iniciado sesión correctamente, es decir, está variabla aumenta 1 unidad
    cada vez que el usuario envía el formulario de login.jsp y los datos enviados son válidos
    El modificador de tipo static hace que la variable pertenezca a la clase o al contexto global,
    y no a una instancia o ejecución concreta, es decir, nos permite utilizar y modificar esta variable
    en cualquier parte de la clase.
     */
    static int contador = 0;
    //Metodo doGet heredado de HttpServlet y sobreescrito
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*
        Crear una instancia de la clase LoginServiceSessionImpl que implementa el metodo de la interfaz
        LoginService
         */
        LoginService auth = new LoginServiceSessionImpl();
        /*Creamos un contenedor que guarda lo que retorna el metodo getUserName, que es el valor
         de nuestra clave username que es un atributo de un objeto HttpSession el cual creamos a partir
         la petición enviada por el cliente
         */
        Optional<String> usernameOptional = auth.getUsername(req);

        /*
         * Utilizamos un try-with-resources para cerrar automáticamente el objeto PrintWriter automáticamente
         * */
        if(usernameOptional.isPresent()) {
            //Establecemos el tipo de contenido de respuesta
            resp.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {

                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Login " +  usernameOptional.get() + "</title>");
                out.println("<link rel=\"stylesheet\" href=\"" + req.getContextPath() + "/css/estilos.css\">");
                out.println("</head>");
                out.println("<body>");
                out.println("<h2> Hola " + "<strong>" +usernameOptional.get() + "</strong>" + " has iniciado sesión correctamente </h2>");
                out.println("<h2>Número de veces que has iniciado sesión: " + contador + "</h2>");
                out.println("<p> <a href='"+req.getContextPath()+"/index.html'>Volver a la página principal</a></p>");
                out.println("<p> <a href='"+req.getContextPath()+"/logout'>Cerrar Sesión</a></p>");
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            /*
             * En caso de que el usernemeOptional no haya retornado un valor, es decir no se haya encontrado un valor para
             * la clave username, el servlet redirige al usuario al formulario para que pueda volver a ingresar sus credenciales
             * pero redirige sin volver a hacer una nueva petición, es un forward interno
             * El getServletContext retorna un objeto que representa a la aplicación web completa
             * El getRequestDispatcher permite enviar una petición a un recurso dentro del servidor
             * con forward(req, resp) transferimos el control al JSP (muestra el formulario)
             * */
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Capturamos los parámetros del formulario
        String username = req.getParameter("user");
        String password = req.getParameter("password");

        //Validamos que las credenciales ingresadas por el usuario desde el formulario sean iguales a nuestras variables estáticas
        if (username.equals(USERNAME) && password.equals(PASSWORD)) {
            /*Establecemos el tipo de contenido en respuesta a la petición
            resp.setContentType("text/html;charset=UTF-8");
            */
            //Creamos un objeto de tipo HttpSession para guardar la sesion de la petición que hizo el usuario
            HttpSession session = req.getSession();
            //Establecemos el atributo username al objeto de tipo session que creamos
            session.setAttribute("username", username);
            //Cada vez que el usuario envíe un formulario con credenciales válidas, la variable contador incrementa una unidad
            contador += 1;

            //El metodo sendRedirect redirecciona automáticamente y crea una nueva petición Get al index.html
            resp.sendRedirect(req.getContextPath()+"/index.html");

        } else {
            //Envíamos a la respuesta de la petición http post un código de estado Http, el 401 (unauthorized)
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Lo sentimos no tiene acceso o credenciales incorrectas");
        }
    }
}
