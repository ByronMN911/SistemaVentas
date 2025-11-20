package models;
/*
 * Autor: Byron Melo
 * Fecha: 12/11/2025
 * Versión: 1.0
 * Descripción: Clase que representa un ítem individual dentro del carrito de compras.
 * Esta clase encapsula la información de un producto específico y la cantidad
 * seleccionada por el usuario para su compra.
 * */

import java.util.Objects;

/**
 * La clase ItemCarro actúa como un contenedor que relaciona un producto específico
 * con la cantidad que el usuario desea comprar.
 * Cada instancia de la clase representa un registro en el carrito de compras.
 */
public class ItemCarro {

    // Se declara la variable privada que almacena la cantidad de unidades del producto
    private int cantidad;

    // Se declara la variable privada que almacena la referencia al objeto Producto asociado
    private Producto producto;

    /**
     * Constructor de la clase ItemCarro que inicializa un nuevo ítem del carrito.
     * Se reciben como parámetros la cantidad deseada y el producto específico.
     * Este constructor establece la relación entre un producto y su cantidad en el carrito.
     *
     * @param cantidad Número de unidades del producto que se agregan al carrito
     * @param producto Objeto Producto que contiene toda la información del artículo
     */
    public ItemCarro(int cantidad, Producto producto){
        this.cantidad = cantidad;
        this.producto = producto;
    }

    /**
     * Declaramos metodos Getters y Setters para acceder a las variables privadas
     * de la clase ItemCarro desde otras clases
     */

    //Metodo getter que permite obtener un objeto producto
    public Producto getProducto(){
        return producto;
    }

    //Metodo setter que permite modificar el objeto producto

    public void setProducto(Producto producto){
        this.producto = producto;
    }

    //Metodo getter que permite obtener la cantidad de unidades del producto.

    public int getCantidad() {
        return cantidad;
    }

    //Metodo setter que permite modificar la cantidad de unidades del producto.
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Se sobrescribe el metodo equals heredado de Object para proporcionar una
     * comparación personalizada entre objetos ItemCarro. Este metodo es crucial
     * para determinar si dos ítems del carrito son equivalentes, lo cual es
     * necesario para evitar duplicados y para operaciones de búsqueda en colecciones.
     *
     * @param o Objeto a comparar con la instancia actual
     * @return true si los objetos son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        /**
         * Primero se verifica si ambas referencias apuntan al mismo objeto en memoria.
         * Si es así, son definitivamente iguales y se retorna true inmediatamente.
         */
        if (this == o) return true;

        /**
         * Se verifica si el objeto a comparar es null o si pertenece a una clase diferente.
         * Si cualquiera de estas condiciones es verdadera, los objetos no pueden ser iguales
         * y se retorna false. Esto previene ClassCastException en el siguiente paso.
         */
        if (o == null || getClass() != o.getClass()) return false;

        /**
         * Se realiza un cast seguro del objeto genérico a ItemCarro, ya que en este punto
         * se ha confirmado que el objeto es de la misma clase. Esto permite acceder a
         * los atributos específicos de ItemCarro para la comparación.
         */
        ItemCarro itemCarro = (ItemCarro) o;

        /**
         * Se comparan los atributos relevantes de ambos objetos ItemCarro.
         * La comparación se basa en el ID del producto (usando Objects.equals para manejar posibles valores null)
         *
         * Se utiliza Objects.equals() para comparar de forma segura, evitando
         * NullPointerException si alguno de los valores es null.
         * Dos ítems son considerados iguales si contienen el mismo producto (mismo ID)
         */
        return Objects.equals(producto.getIdProducto(), itemCarro.producto.getIdProducto());

    }

    /*
     * Como sobreescribimos equals(), también debemos sobrescribir hashCode(), es una obligación
     *  del contrato de Java, si no sobreescribimos este metodo puede ocurri lo siguiente:
     * contains() puede fallar si internamente usa hash (por ejemplo en un HashSet).
     * remove() y indexOf() (basado en hash) puede fallar.
     * En nuestro código si lo sobreescribimos no pasa nada porque nosotros en la clase DetalleCarro
     * estamos utilzando contains() que internamente usa el metodo equals() sobreescrito pero contains()
     * se aplica a una lista y las listas no usan hash sino comparaciones secuenciales con equals()
     * */
    @Override
    public int hashCode() {
        return Objects.hash(producto.getIdProducto());
    }

    /**
     * Metodo que calcula el subtotal para este ítem del carrito.
     * El subtotal se obtiene multiplicando la cantidad de unidades por el precio
     * unitario del producto.
     */
    public double getSubtotal() {
        // Se multiplica la cantidad por el precio del producto y se retorna el resultado
        return cantidad * producto.getPrecio();
    }
}