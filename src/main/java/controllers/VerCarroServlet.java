package controllers;
/*
 * Autor: Byron Melo
 * Fecha: 13/11/2025
 * Versión: 1.0
 * Descripción: Esta clase es un servlet que al utilizarlo lo que hace es redireccionar al usuario
 * a carro.jsp, que es nuestra vista para mostrar el carrito de compras del usuario, este redireccionamiento
 * lo hace de forma interna con .getRequestDispatcher, dándole el control al jsp.
 * */
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ver-carro")
public class VerCarroServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
         * el servlet redirige al usuario al jsp para ver el carrito de compras,
         * pero redirige sin volver a hacer una nueva petición, es un forward interno
         * El getServletContext retorna un objeto que representa a la aplicación web completa
         * El getRequestDispatcher permite enviar una petición a un recurso dentro del servidor
         * con forward(req, resp) transferimos el control al JSP (muestra el formulario)
         * */
        getServletContext().getRequestDispatcher("/carro.jsp").forward(req,resp);
    }
}
