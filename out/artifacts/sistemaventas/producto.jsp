<%--
/**
 * Autor: Byron Melo
 * Fecha: 20/11/2025
 * Versión: 1.0
 * Descripción:
 * Esta página JSP (JavaServer Page) se encarga de mostrar un listado de productos.
 * Muestra información diferente (como el precio) y la opción de crear un producto
 * solo si un usuario ha iniciado sesión. También incluye una funcionalidad para
 * agregar productos al carrito de compras.
 */
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="java.util.*, models.*" %>
<%----%>

<%
    // Recupera la lista de productos almacenada en la solicitud (request) por el Servlet.
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    // Recupera el nombre de usuario (si existe) también del objeto request.
    // Se usa Optional<String> para manejar la posible ausencia de un usuario logueado.
    Optional<String> username = (Optional<String>) request.getAttribute("username");
%>

<html>
<head>
    <title>Listado Productos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
    <link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css'>
</head>
<body>
<h1>Listado de productos</h1>
<% // Verifica si el nombre de usuario está presente, indicando que hay una sesión activa.
    if ( username.isPresent()) { %>
<div>Hola <%=username.get()%>, Bienvenido!</div>
<p><a href="<%=request.getContextPath()%>/crear">Crear un producto</a></p>
<%}%>
<table>
    <tr>
        <th>ID Producto</th>
        <th>Nombre del Producto</th>
        <th>Categoría</th>
        <th>Stock</th>
        <th>Descripción</th>
        <th>Código</th>
        <th>Fecha Elaboración</th>
        <th>Fecha Caducidad</th>
        <th>Condición</th>
        <% // Columna 'Precio' solo se muestra si el usuario está logueado.
            if ( username.isPresent()) { %>
        <th>Precio</th>
        <th>Acción</th>
        <%}%>

    </tr>
    <% // Itera sobre la lista de productos para generar una fila (<tr>) por cada uno.
        for (Producto p : productos) { %>
    <tr>
        <td><%=p.getId()%>
        </td>
        <td><%=p.getNombre()%>
        </td>
        <td><%=p.getCategoria().getNombre()%>
        </td>
        <td><%=p.getStock()%>
        </td>
        <td><%=p.getDescripcion()%>
        </td>
        <td><%=p.getCodigo()%>
        </td>
        <td><%=p.getFechaElaboracion()%>
        </td>
        <td><%=p.getFechaCaducidad()%>
        </td>
        <td><%=p.getCondicion()%>
        </td>
        <% // Muestra el precio del producto solo si el usuario está logueado.
            if ( username.isPresent()) { %>
        <td><%=p.getPrecio()%>
        </td>
        <%--
        /**
         * La celda de acción contiene el formulario para agregar el producto al carrito.
         * Se usa un formulario GET para simplificar, enviando el ID del producto
         * a la ruta '/agregar-carro'.
         */
        --%>
        <td>
            <%-- Utilizamos el formulario GET para enviar el ID --%>
            <form action="<%=request.getContextPath()%>/agregar-carro" method="get" class="form-inline-icon">
                <%-- Campo oculto para enviar el ID del producto --%>
                <input type="hidden" name="id" value="<%=p.getId()%>">
                <%-- Botón con el ícono de Font Awesome para agregar al carrito --%>
                <button type="submit" class="icon-button" title="Agregar al carrito">
                    <i class="fa fa-shopping-cart" aria-hidden="true"></i>

                </button>
            </form>
        </td>
        <%}%>

    </tr>
    <%}%>
</table>

</body>
</html>