<%--
  Autor: Byron Melo y Alan Olivo
  Fecha: 21-11-2025
  Version: 1.0
  Descripción: Este JPS se encarga de mostrar un formulario para crear o actualizar la información
  de un producto usando el Hashmap que mapea y almacena errores de nuestro ProductoFormServlet.java
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
import="java.util.*, java.time.format.*, models.*"%>
<%
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    Map<String, String> errores = (Map<String, String>) request.getAttribute("errores");
    Producto producto = (Producto) request.getAttribute("producto");
    String fechaElaboracion = producto.getFechaElaboracion()!=null?
    producto.getFechaElaboracion().format(DateTimeFormatter.ofPattern("yyyy-MM--dd")):"";
    String fechaCaducidad = producto.getFechaCaducidad()!=null?
            producto.getFechaCaducidad().format(DateTimeFormatter.ofPattern("yyyy-MM--dd")):"";
    %>

<html>
<head>
    <title>Formulario Productos</title>
</head>
<body>
<form action="<%=request.getContextPath()%>/crear" method="post">
    <div>
        <label for="nombre">Nombre</label>
        <input type="text" name="nombre" id="nombre">
        <% if (errores != null && errores.containsKey("nombre")) {%>
        <div style="color: red"><%=errores.get("nombre")%></div>
        <% } %>
    </div>

    <div>
        <label for="categoria">Categoria</label>
        <select name="categoria" id="categoria">
            <option value="">------Seleccionar------</option>
            <% for (Categoria c: categorias) { %>
            <option value="<%=c.getId()%>"
                    <%=c.getId().equals(producto.getCategoria().getId())?" selected":"" %>>
                <%=c.getNombre()%>
            </option>
            <% } %>
        </select>
        <% if (errores != null && errores.containsKey("categoria")) {%>
        <div style="color: red"><%=errores.get("categoria")%></div>
        <% } %>
    </div>

    <div>
        <label for="stock">Ingrese el stock</label>
        <input type="number" name="stock" id="stock">
        <% if (errores != null && errores.containsKey("stock")) {%>
        <div style="color: red"><%=errores.get("stock")%></div>
        <% } %>
    </div>

    <div>
        <label for="precio">Precio</label>
        <input type="number" step="0.01" name="precio" id="precio">
        <% if (errores != null && errores.containsKey("precio")) {%>
        <div style="color: red"><%=errores.get("precio")%></div>
        <% } %>
    </div>
    <div>
        <label for="descripcion">Descripción</label>
        <div>
            <textarea name="descripcion" id="descripcion" cols="30" rows="10"></textarea>
        </div>
    </div>

    <div>
        <label for="codigo">Código</label>
        <div>
            <input type="text" name="codigo" id="codigo">
        </div>
        <% if (errores != null && errores.containsKey("codigo")) {%>
        <div style="color: red"><%=errores.get("codigo")%></div>
        <% } %>
    </div>

    <div>
        <label for="fecha_elaboracion">Fecha de Elaboración</label>
        <div>
            <input type="date" name="fecha_elaboracion" id="fecha_elaboracion">
        </div>
        <% if (errores != null && errores.containsKey("fecha_elaboracion")) {%>
        <div style="color: red"><%=errores.get("fecha_elaboracion")%></div>
        <% } %>
    </div>

    <div>
        <label for="fecha_caducidad">Fecha de Elaboración</label>
        <div>
            <input type="date" name="fecha_caducidad" id="fecha_caducidad">
        </div>
        <% if (errores != null && errores.containsKey("fecha_caducidad")) {%>
        <div style="color: red"><%=errores.get("fecha_caducidad")%></div>
        <% } %>
    </div>

    <div>
        <input type="submit" value="<%= (producto.getId() != null && producto.getId() > 0) ? "Editar" : "Crear" %>">
        <input type="hidden" name="id" value="<%=producto.getId()%>">
    </div>
</form>
</body>
</html>

