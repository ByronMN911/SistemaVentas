package services;
/*
 * Autor: Byron Melo
 * Fecha: 19/11/2025
 * Versión: 1.0
 * Descripción:
 * esta clase nos va a permitir crear errores personalizados dentro de la capa de servicios
 *  o DAOS, esto evita exponer directamente SQLException a las capas superiores de esta manera
 *  mantenemos el código limpio y profesional en aplicación Jakarta EE.
 *
 * ProductoRepositoryJdbcImplement.java es una clase DAO ya que estamos utilizando metodos para
 * acceder a la base de datos con el objetivo de ver, actualizar, eliminar o crear registros en ella.
 * */

public class ServiceJbdcException extends RuntimeException{
    /*
     * implementamos un constructor que invoca al constructor padres
     * y recibe un parámetro de tipo String llamando mensaje
     *
     */
    public ServiceJbdcException(String message){
        //Llamamos al contructor padre para mostrar el mensaje
        super(message);
    }
    /*
     * implementamos un constructor que recibe dos parámetros
     * de tipo String message y tambien la causa del error
     *
     */
    public ServiceJbdcException(String message, Throwable cause){
        super(message, cause);
    }
}
