package controllers;
/*
 * Autor: Byron Melo
 * Fecha: 11/11/2025
 * Versión: 1.0
 * Descripción: Esta clase hereda de HttpServlet para manejar peticiones get mediante la llave logout
 * El metodo doGet se encarga de usar el metodo implementado por la clase LoginServiceSessionImpl que retorna
 * un contenedor Optional<String> el cual tiene el valor de la clave username de una sesión.
 * Si ese Contenedor tiene un valor entonces se obtiene la sesión (HTTPSesion) y se invalida, es decir,
 * se cierra la sesión y redirige al usuario a login.jsp
 * */
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.LoginService;
import services.LoginServiceSessionImpl;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*
        Creamos una instancia de la interfaz LoginService la cual tiene un metodo declarado,
        luego este objeto hace referencia a la clase LoginServiceSessionImpl() que implementa el
        metodo de la interfaz
         */
        LoginService auth = new LoginServiceSessionImpl();
        /*
        Creamos un contendor Optional que guarda un valor String que es el atributo de la sesion,
        en otras palabras es el username.
        Este valor se retorna al usar el metodo getUserName
         */
        Optional<String> username = auth.getUsername(req);

        /*
        Si el contenedor username tiene un valor (atributo de la sesión) entonces se crea un objeto de tipo HttpSession
        llamado session que tiene una sesión de la petición enviada por el usuario desde el navegador.
         */
        if(username.isPresent()) {
            HttpSession session = req.getSession();
            /*Una vez que se ha validado que el contenedor username tiene un valor, es decir, existe una sesión activa
            y posteriormente hemos obtenido la sesión de la petición, procedemos a invalidar la sesión correspondiente
            a un usuario en específico con el metodo invalidate()
            * */
            session.invalidate();
        }
        /*Luego de invalidar la sesión procedemos a enviar al usuario a login.html
        utilizamos req.getContextPath porque esto nos retorna el protocolo, el tipo de servidor, el puerto y el nombre del proyecto
        por ello se concatena con la llave login.html de LoginServlet.java: http://localhost:8080/manejosesiones/index.html
        */
        resp.sendRedirect(req.getContextPath()+"/login.html");
    }
}
