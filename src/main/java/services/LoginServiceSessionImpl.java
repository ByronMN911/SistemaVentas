package services;
/*
 * Autor: Byron Melo
 * Fecha: 11/11/2025
 * Versión: 1.0
 * Descripción: Esta clase implementa el metodo de la interfaz LoginService, este metodo retorna
 * un contenedor Optional<String> que puede o no tener el valor de la clave username de la sesión
 * (HTTPSesion) de un usuario, la cual se genera cuando realiza una petición HTTP.
 * */
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class LoginServiceSessionImpl implements LoginService{
    @Override
    public Optional<String> getUsername(HttpServletRequest request) {
        // Creamos un objeto HttpSession que guardará la sesión que se creó cuando
        //el usuario hizo una petición Http Get al servlet
        HttpSession session = request.getSession();
        /*
        Creamos una variable de tipo String que guarda el atributo de la sesión
        Primero obtenemos el atributo de la clave username (nombre del usuario)
         y luego lo transformamos a String
        */

        String username = (String) session.getAttribute("username");
        /*Estructura condicional que a través de una condición la cual dice:
        Si la variable String usuario no está vacía entonces se retorna un contenedor
        Optional con el atributo de la sesión que es el username y en caso de que username
        esté vacío retorna el contenedor Optional vacío.
        */
        if(username !=null){
            return Optional.of(username);
        }
        return Optional.empty();
    }
}
