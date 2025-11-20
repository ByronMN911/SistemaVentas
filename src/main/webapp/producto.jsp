<%--
  Created by IntelliJ IDEA.
  User: Arman
  Date: 20/11/2025
  Time: 9:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="java.util.*, models.*" %>
<%----%>

<%
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    Optional<String> username = (Optional<String>) request.getAttribute("username");
%>

<html>
<head>
    <title>Listado Productos</title>
</head>
<body>
<h1>Listado de productos</h1>
<% if ( username.isPresent()) { %>
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
        <% if ( username.isPresent()) { %>
        <th>Precio</th>
        <%}%>
        <th>Acción</th>
    </tr>
    <% for (Producto p : productos) { %>
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
        <% if ( username.isPresent()) { %>
        <td><%=p.getPrecio()%>
        </td>
        <%}%>
        <%-- En caso de que el botón solo se muestre a usuarios logueados debes meterlo dentro del if--%>
        <td>
            <%-- Utilizamos el formulario GET para enviar el ID --%>
            <form action="<%=request.getContextPath()%>/agregar-carro" method="get" class="form-inline-icon">
                <%-- Campo oculto para enviar el ID del producto --%>
                <input type="hidden" name="id" value="<%=p.getId()%>">
                <%-- Botón con el ícono de Font Awesome --%>
                <button type="submit" class="icon-button" title="Agregar al carrito">
                    <i class="fa fa-shopping-cart" aria-hidden="true"></i>
                    <%-- Puedes añadir texto si lo deseas: "Agregar" --%>
                </button>
            </form>
        </td>
    </tr>
    <%}%>
</table>

</body>
</html>