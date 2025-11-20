package models;
/*
Autor: Byron Melo
Fecha: 17/11/2025
Versión: 1.0
Descripción:
En esta clase definimos los atributos que tendrá un objeto Categoria, estos atributos
deben ser los mismos campos de la tabla Categoria de nuestra base de datos SistemaVentas.
*/


/*
 Creamos variables con el modificador private para luego crear getters y setters de estos
 atributos y que otras clases puedan acceder y modificar su valor.
 */
public class Categoria{
    private Long id;
    private String nombre;
    private String descripcion;
    private int estado;

    //Implementamos el constructor vacío
    public Categoria() {
    }

    /*Este constructor recibe como parámetros sus atributos propios
     Se inicializan los atributos de la clase usando los parámetros del constructor
    */
    public Categoria(Long id, String nombre, String descripcion, int estado ){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    //Implementamos los métodos Getter y setter

    ///Obtener el ID de una categoria
    public Long getId() {
        return id;
    }

    //Modificar el ID de una categoria
    public void setId(Long id) {
        this.id = id;
    }

    ///Obtener el nombre de la categoria, se retorna el atributo nombre
    public String getNombre() {
        return nombre;
    }

    //Se modifica el atributo nombre de la categoria
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //Se obtiene la descripción de un producto
    public String getDescripcion() {
        return descripcion;
    }

    //Se modifica la descrición de un producto
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    //Se obtiene el estado de un producto
    public int getEstado() {
        return estado;
    }

    //Se modifica el estado de un producto
    public void setEstado(int estado) {
        this.estado = estado;
    }
}