package models;
/*
 * Autor: Byron Melo
 * Fecha: 12/11/2025
 * Versión: 1.0
 * Descripción: Clase que representa el detalle completo del carrito de compras.
 * Esta clase gestiona la colección de todos los ítems (productos) que el usuario
 * ha agregado a su carrito, proporcionando funcionalidades para agregar productos,
 * calcular totales y mantener la lista actualizada de compras.
 * */

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * La clase DetalleCarro actúa como el contenedor principal que administra todos
 * los ítems del carrito de compras. Es responsable de mantener la integridad
 * de la lista de productos, evitar duplicados y proporcionar cálculos totales.
 */
public class DetalleCarro {

    /**
     * Se declara una lista privada que almacenará todos los objetos ItemCarro.
     * Esta colección representa el contenido completo del carrito de compras.
     * Se utiliza la interfaz List para mayor flexibilidad y la implementación
     * será ArrayList.
     */
    private List<ItemCarro> items;

    /**
     * Constructor por defecto de la clase DetalleCarro.
     * Se inicializa la lista de ítems como un ArrayList vacío, preparando
     * el carrito para recibir productos. Al usar ArrayList se garantiza un
     * acceso rápido por índice y una inserción eficiente al final de la lista.
     */
    public DetalleCarro() {
        this.items = new ArrayList<>();
    }

    /**
     * Metodo fundamental que permite agregar un producto al carrito de compras.
     * Este metodo que verifica si el producto ya existe en el carrito, si ya existe
     * incrementa su cantidad en 1 unidad y si no existe lo agrega como un nuevo ítem a la lista
     *
     * Esta aproximación evita tener productos duplicados en el carrito y en su lugar
     * acumula las cantidades.
     *
     * @param itemCarro El ítem (producto con cantidad) que se desea agregar al carrito
     */
    public void addItemCarro(ItemCarro itemCarro) {
        /**
         * Se verifica si el ítem ya existe en la lista utilizando el metodo contains().
         * Este metodo internamente usa el equals() que se sobrescribió en ItemCarro,
         * comparando por ID de producto y cantidad.
         */
        if(items.contains(itemCarro)) {
            /**
             * Si el producto ya existe en el carrito, se procede a buscar ese ítem
             * específico usando Streams. Este bloque implementa la lógica
             * para incrementar la cantidad del producto existente.
             */

            /**
             * Se utiliza la API de Streams para buscar el ítem existente:
             * 1. items.stream() convierte la lista en un flujo de datos procesable
             * 2. filter() se encarga filtra el stream para encontrar el ítem que coincida
             * 3. i -> i.equals(itemCarro) es una expresión lambda que compara cada ítem con el nuevo
             * 4. findAny() retorna cualquier elemento que cumpla el filtro envuelto en Optional
             *
             * Se retorna un Optional<ItemCarro> que puede o no contener el ítem encontrado.
             */
            Optional<ItemCarro> optionalItemCarro = items.stream()
                    .filter(i -> i.equals(itemCarro))
                    .findAny();

            /**
             * Se verifica si el Optional contiene un valor (es decir, si se encontró el ítem).
             * isPresent() retorna true si existe un valor, false si está vacío.
             * Esta verificación adicional asegura que el ítem realmente exista antes de
             * intentar acceder a él, evitando posibles excepciones.
             */
            if(optionalItemCarro.isPresent()) {
                /**
                 * Se extrae el objeto ItemCarro del Optional usando get().
                 * En este punto se tiene la garantía de que el valor existe gracias
                 * a la validación previa con isPresent().
                 */
                ItemCarro i = optionalItemCarro.get();

                /**
                 * Se incrementa la cantidad del ítem existente en 1 unidad.
                 * Se obtiene la cantidad actual con getCantidad(), se le suma 1,
                 * y se establece el nuevo valor con setCantidad().
                 * Esta es la operación clave que acumula productos en lugar de duplicarlos.
                 */
                i.setCantidad(i.getCantidad() + 1);
            }
        }else{
            /**
             * Si el producto NO existe en el carrito (el contains() retornó false),
             * simplemente se agrega el nuevo ítem a la lista.
             * Este es el caso cuando el usuario agrega un producto por primera vez.
             */
            this.items.add(itemCarro);
        }
    }

    /**
     * Metodo getter que permite obtener la lista completa de ítems del carrito.
     * Este metodo es esencial para que otras clases puedan acceder a los productos
     * del carrito, por ejemplo, para mostrarlos en el JSP o para generar la factura de compra.
     */
    public List<ItemCarro> getItem() {
        return items;
    }

    /**
     * Metodo que calcula y retorna el total general del carrito de compras.
     * El proceso se realiza en tres pasos:
     * 1. items.stream() convierte la lista de ítems en un Stream
     * 2. mapToDouble(ItemCarro::getSubtotal) transforma cada ItemCarro en su subtotal (double)
     *    usando una referencia al metodo getSubtotal()
     * 3. sum() se encarga suma todos los valores double del stream resultante
     */

    public double getSubtotal() {
        //Este calculo representa la suma de todos los precios unitarios del carrito de compras
        return items.stream().mapToDouble(ItemCarro::getSubtotal).sum();
    }

    public double getSubtotalIva(){
        //Obtenemos el IVA del 15% de todos los productos del carrito de compras
        return getSubtotal()*0.15;
    }

    public double getTotal(){
        //Sumamos la suma de los precios unitarios de todos los productos del carrito de compras
        //con el IVA del 15% de todos los productos del carrito de compras
        return getSubtotal() + getSubtotalIva();
    }


}