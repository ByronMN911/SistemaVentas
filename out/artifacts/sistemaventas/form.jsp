<%--
  Autor: Byron Melo y Alan Olivo
  Fecha: 21-11-2025
  Version: 1.0
  Descripción: Este JPS se encarga de mostrar un formulario para crear o actualizar la información
  de un producto usando el Hashmap que mapea y almacena errores en nuestro ProductoFormServlet.java
--%>
<%-- Directivas JSP: Define el tipo de contenido, el lenguaje y las importaciones de clases Java necesarias --%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="java.util.*, java.time.format.*, models.*"%>
<%
    // Bloque Scriptlet: Código Java que se ejecuta al procesar la página JSP.

    // 1. Obtención de datos del 'request' (enviados desde el Servlet)
    // Se obtiene la lista de categorías disponible para el selector del formulario.
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    // Se obtiene el mapa de errores de validación (HashMap<campo, mensaje>) del formulario.
    Map<String, String> errores = (Map<String, String>) request.getAttribute("errores");
    // Se obtiene el objeto Producto que se está editando o creando (contiene valores previos/a editar).
    Producto producto = (Producto) request.getAttribute("producto");

    // 2. Formateo de fechas para campos 'input type="date"'
    // Se formatea la fecha de elaboración a String en formato "yyyy-MM--dd" si no es null.
    String fechaElaboracion = producto.getFechaElaboracion()!=null?
            producto.getFechaElaboracion().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")):"";
    // Se formatea la fecha de caducidad a String en formato "yyyy-MM--dd" si no es null.
    String fechaCaducidad = producto.getFechaCaducidad()!=null?
            producto.getFechaCaducidad().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")):"";
%>

<html>
<head>
    <title>Formulario Productos</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
    <link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css'>
</head>
<body>
<%-- Formulario HTML: El 'action' apunta al servlet que manejará la creación/actualización. --%>
<form action="<%=request.getContextPath()%>/crear" method="post">

    <%-- Campo Nombre --%>
    <div>
        <label for="nombre">Nombre</label>
        <%-- El atributo 'value' debería establecerse con el valor actual del producto si existiera. (Faltaría aquí: value="<%=producto.getNombre()%>") --%>
        <input type="text" name="nombre" id="nombre" value="<%=producto.getNombre() != null ? producto.getNombre() : "" %>">
        <%-- Muestra error si existe una entrada con la clave "nombre" en el mapa de errores. --%>
        <% if (errores != null && errores.containsKey("nombre")) {%>
        <div class="error-message"><%=errores.get("nombre")%></div>
        <% } %>
    </div>

    <%-- Campo Categoría (Select/Dropdown) --%>
    <div>
        <label for="categoria">Categoria</label>
        <select name="categoria" id="categoria">
            <option value="">------Seleccionar------</option>
            <%-- Itera sobre la lista de categorías obtenida del request. --%>
            <% for (Categoria c: categorias) { %>
            <option value="<%=c.getId()%>"
            <%-- Condición para pre-seleccionar la categoría actual del producto. --%>
                    <%= (producto.getCategoria() != null && c.getId().equals(producto.getCategoria().getId())) ? " selected" : "" %>>
                <%=c.getNombre()%>
            </option>
            <% } %>
        </select>
        <%-- Muestra error si existe una entrada con la clave "categoria" en el mapa de errores. --%>
        <% if (errores != null && errores.containsKey("categoria")) {%>
        <div class="error-message"><%=errores.get("categoria")%></div>
        <% } %>
    </div>

    <%-- Campo Stock --%>
    <div>
        <label for="stock">Ingrese el stock</label>
        <%-- Se usa 'type="number"' para forzar entrada numérica. --%>
        <input type="number" name="stock" id="stock" value="<%=producto.getStock()%>">
        <%-- Muestra error si existe una entrada con la clave "stock" en el mapa de errores. --%>
        <% if (errores != null && errores.containsKey("stock")) {%>
        <div class="error-message"><%=errores.get("stock")%></div>
        <% } %>
    </div>

    <%-- Campo Precio --%>
    <div>
        <label for="precio">Precio</label>
        <div>
        <%-- 'step="0.01"' permite decimales.--%>
        <input type="number" step="0.01" name="precio" id="precio" value="<%=producto.getPrecio()%>">
        <%-- Muestra error si existe una entrada con la clave "precio" en el mapa de errores. --%>
        </div>
            <% if (errores != null && errores.containsKey("precio")) {%>
            <div class="error-message"><%=errores.get("precio")%></div>
            <% } %>
        </div>

        <%-- Campo Descripción (Área de texto) --%>
        <div>
            <label for="descripcion">Descripción</label>
            <div>
                <%-- El contenido actual de la descripción se inserta dentro del tag textarea. --%>
                <textarea name="descripcion" id="descripcion" cols="30" rows="10"><%=producto.getDescripcion() != null ? producto.getDescripcion() : "" %></textarea>
            </div>
        </div>

        <%-- Campo Código --%>
        <div>
            <label for="codigo">Código</label>
            <div>
                <%-- (Faltaría aquí: value="<%=producto.getCodigo()%>") --%>
                <input type="text" name="codigo" id="codigo" value="<%=producto.getCodigo() != null ? producto.getCodigo() : "" %>">
            </div>
            <%-- Muestra error si existe una entrada con la clave "codigo" en el mapa de errores. --%>
            <% if (errores != null && errores.containsKey("codigo")) {%>
            <div class="error-message"><%=errores.get("codigo")%></div>
            <% } %>
        </div>

        <%-- Campo Fecha de Elaboración --%>
        <div>
            <label for="fecha_elaboracion">Fecha de Elaboración</label>
            <div>
                <%-- 'type="date"' y se usa la variable formateada para establecer el valor actual. --%>
                <input type="date" name="fecha_elaboracion" id="fecha_elaboracion" value="<%=fechaElaboracion%>">
            </div>
            <%-- Muestra error si existe una entrada con la clave "fecha_elaboracion" en el mapa de errores. --%>
            <% if (errores != null && errores.containsKey("fecha_elaboracion")) {%>
            <div class="error-message"><%=errores.get("fecha_elaboracion")%></div>
            <% } %>
        </div>

        <%-- Campo Fecha de Caducidad --%>
        <div>
            <label for="fecha_caducidad">Fecha de Caducidad</label>
            <div>
                <%-- 'type="date"' y se usa la variable formateada para establecer el valor actual. --%>
                <input type="date" name="fecha_caducidad" id="fecha_caducidad" value="<%=fechaCaducidad%>">
            </div>
            <%-- Muestra error si existe una entrada con la clave "fecha_caducidad" en el mapa de errores. --%>
            <% if (errores != null && errores.containsKey("fecha_caducidad")) {%>
            <div class="error-message"><%=errores.get("fecha_caducidad")%></div>
            <% } %>
        </div>

        <%-- Botones de Envío y Campo Oculto para ID --%>
        <div>
            <%-- El texto del botón cambia dinámicamente según si se está editando (producto.getId() > 0) o creando. --%>
            <input type="submit" value="<%= (producto.getId() != null && producto.getId() > 0) ? "Editar" : "Crear" %>">
            <%-- Campo oculto: Necesario para enviar el ID del producto al servlet cuando se está editando. --%>
            <input type="hidden" name="id" value="<%=producto.getId() != null ? producto.getId() : "" %>">
        </div>
</form>
</body>
</html>