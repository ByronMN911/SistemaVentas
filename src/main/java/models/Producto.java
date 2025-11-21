package models;

/*
 * Autor: Byron Melo
 * Fecha: 19/11/2025
 * Versión: 1.0
 * Descripción: Esta clase define los atributos que tendrá un objeto Producto y además definimos
 * metodos getters y setters para acceder y modificar estos atributos.
 * Estos atributos hacen referencia a los mismos campos de la tabla Producto de nuestra base de datos
 * llamada "sistemaventas".
 * */

// Se importa LocalDate para manejar fechas (caducidad y elaboración).
import java.time.LocalDate;


public class Producto {
    // Identificador único del producto (clave primaria usualmente).
    private Long id;

    // Nombre del producto.
    private String nombreProducto;

    // Relación con la clase Categoria (un producto pertenece a una categoría).
    private Categoria categoria;

    // Precio del producto.
    private double precio;

    // Cantidad disponible en inventario.
    private int stock;

    // Descripción general del producto.
    private String descripcion;

    //Codigo de un producto
    private String codigo;

    // Fecha de elaboración del producto.
    private LocalDate fechaElaboracion;

    // Fecha de caducidad del producto.
    private LocalDate fechaCaducidad;

    // Estado o condición (posiblemente disponible, vencido, etc.).
    private int condicion;




/*
Constructor vacío (obligatorio para frameworks que requieren
instanciación sin parámetros, como Hibernate, Spring, etc.)
 */
    public Producto() {
    }



// Constructor con parámetros para inicializar el objeto Producto completo

    public Producto(Long id, String nombreProducto, double precio, String codigo, String descripcion, int stock, String tipo, LocalDate fechaElaboracion, LocalDate fechaCaducidad, int condicion) {
        //Inicializamos los atributos con los parámetros del constructor
        this.id = id;
        this.nombreProducto = nombreProducto;
        this.condicion = condicion;

        // Crea una categoría nueva y establece su nombre según el "tipo".
        Categoria categoria = new Categoria();
        categoria.setNombre(tipo);

        // Asigna descripción, stock, precio y nombre.
        this.precio = precio;
        this.codigo = codigo;
        this.stock = stock;
        this.descripcion = descripcion;

        // Asigna las fechas.
        this.fechaCaducidad= fechaCaducidad;
        this.fechaElaboracion= fechaElaboracion;

    }



// Métodos GETTERS Y SETTERS que permiten acceder y modificar atributos privados.


    public Long getId() {
        return id; // Devuelve el ID del producto.
    }

    public void setId(Long id) {
        this.id = id; // Asigna un nuevo ID.
    }

    public int getCondicion() {
        return condicion; // Devuelve la condición.
    }

    public void setCondicion(int condicion) {
        this.condicion = condicion;
    }

    public LocalDate getFechaCaducidad() {
        return fechaCaducidad; // Devuelve la fecha de caducidad.
    }

    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad; // Actualiza la fecha de caducidad.
    }

    public LocalDate getFechaElaboracion() {
        return fechaElaboracion; // Devuelve la fecha de elaboración.
    }

    public void setFechaElaboracion(LocalDate fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion; // Actualiza la fecha de elaboración.
    }

    public String getDescripcion() {
        return descripcion; // Devuelve la descripción.
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion; // Actualiza la descripción.
    }

    public int getStock() {
        return stock; // Devuelve la cantidad en stock.
    }

    public void setStock(int stock) {
        this.stock = stock; // Actualiza el stock.
    }

    public double getPrecio() {
        return precio; // Devuelve el precio.
    }

    public void setPrecio(double precio) {
        this.precio = precio; // Actualiza el precio.
    }

    public String getCodigo() {
        return codigo; // Devuelve el código
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo; //Actualiza un nuevo código al producto
    }

    public Categoria getCategoria() {
        return categoria; // Devuelve la categoría asociada.
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria; // Asigna la categoría al producto.
    }

    public String getNombre() {
        return nombreProducto; // Devuelve el nombre del producto.
    }

    public void setNombre(String nombre) {
        this.nombreProducto = nombre; // Actualiza el nombre del producto.
    }
}
