<%--
    Autor: Byron Melo
    Fecha: 13/11/2025
    Versión: 1.0
    Descripción:
    Esta página JSP muestra el contenido del carrito de compras del usuario.
    La información es tomada desde la sesión y renderizada en una tabla HTML.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!--
Directiva de página:
- contentType define que la salida será HTML en codificación UTF-8.
- language indica que el código Java incrustado será interpretado por el contenedor JSP.
-->

<!--
Directivas import:
Permiten usar las clases Java ItemCarro y DetalleCarro
dentro del código Java del JSP.
-->
<%@ page import="models.ItemCarro" %>
<%@ page import="models.DetalleCarro" %>


<%
    /*SCRIPTLET:
Lo que está dentro del menor % y mayor % es código Java que se ejecuta del lado del servidor
antes de enviar el HTML al cliente.
Recuperamos desde la sesión el atributo llamado "carro", que contiene
un objeto de tipo DetalleCarro y este objeto almacena:
- La lista de productos en el carrito.
- Los cálculos de subtotal, IVA y total.
*/
    DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");
%>

<html>
<head>
    <title>Carro de Compras</title>

    <!--
      Se genera dinámicamente la ruta absoluta hacia el archivo CSS,
      utilizando el contextPath del proyecto (ej: /miApp).
    -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>

<body>
<h1>Carro de Compras</h1>

<%
    /*
        SCRIPTLET condicional:
        Validamos si el carrito existe en sesión y si contiene productos.
        Si el carrito está vacío o no existe, mostramos un mensaje al usuario.
    */
    if(detalleCarro == null || detalleCarro.getItem().isEmpty()) {
%>

<!-- Mensaje simple en HTML -->
<p>Lo sentimos, no hay productos en el carrito!</p>

<%
    /*
        Este else se ejecuta cuando SÍ hay objetos ItemCarro en la lista.
    */
} else {
%>

<!--
    Inicia la tabla HTML donde se mostrarán los productos agregados.
-->
<table>
    <tr>
        <!-- Encabezados de la tabla -->
        <th>ID PRODUCTO</th>
        <th>NOMBRE</th>
        <th>PRECIO</th>
        <th>CANTIDAD</th>
        <th>VALOR</th>
    </tr>

    <%
        /*
            SCRIPTLET - BUCLE FOR:
            Recorremos la lista de ItemCarro dentro del objeto detalleCarro.
            Cada ItemCarro representa un producto seleccionado por el usuario.
        */
        for (ItemCarro item : detalleCarro.getItem()) {
    %>

    <tr>
        <!--
            EXPRESSION
            Cada celda imprime un valor del objeto Java.
        -->
        <td><%= item.getProducto().getIdProducto() %></td>
        <td><%= item.getProducto().getNombre() %></td>
        <td><%= item.getProducto().getPrecio() %></td>
        <td><%= item.getCantidad() %></td>
        <td><%= item.getSubtotal() %></td>
    </tr>

    <% } %>

    <!-- Fila del Subtotal -->
    <tr>
        <td colspan="4" style="text-align:right; font-weight:bold;">Subtotal:</td>
        <!-- Imprimimos el subtotal calculado en la clase DetalleCarro -->
        <td><%= detalleCarro.getSubtotal() %></td>
    </tr>

    <!-- Fila del IVA -->
    <tr>
        <td colspan="4" style="text-align:right; font-weight:bold;">IVA (15%):</td>
        <!-- El IVA es calculado en el metodo getSubtotalIva() -->
        <td><%= detalleCarro.getSubtotalIva() %></td>
    </tr>

    <!-- Fila del Total -->
    <tr>
        <td colspan="4" style="text-align:right; font-weight:bold;">Total a Pagar:</td>
        <!-- Total final -->
        <td><%= detalleCarro.getTotal() %></td>
    </tr>

</table>

<%
    } // Fin del bloque else, aunque solo sea una llave sigue siendo código java, por eso usamos scriptles
%>

<!-- Enlaces que utilizan las llaves de los servlets para acceder a sus funcionalidades -->
<p><a href="<%=request.getContextPath()%>/productos">Seguir Comprando</a></p>
<p><a href="<%=request.getContextPath()%>/index.html">Volver a la Página Principal</a></p>
<p><a href="<%=request.getContextPath()%>/descargar-factura">Descargar Factura (PDF)</a></p>

</body>
</html>
