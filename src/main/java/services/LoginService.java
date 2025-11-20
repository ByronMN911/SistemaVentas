package services;
/*
 * Autor: Byron Melo
 * Fecha: 11/11/2025
 * Versión: 1.0
 * Descripción: Este es un tipo especial de clase, ya que es una interfaz que declara un
 * metodo pero no se implementa su lógica, ya que eso lo hará otra clase.
 * */
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface LoginService {
    Optional<String> getUsername(HttpServletRequest request);
}
