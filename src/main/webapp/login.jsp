<%--
  Usuario: Armando
  Fecha: 10/11/2025
  Versión: 1.0
  Descripción: Esta es una página JSP que muestra un formulario creado con código HTML,
  el envío del formulario utiliza el metodo post y la llave del LoginServlet que es login
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inicio de Sesión</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/estilos.css">
</head>
<body>
<h1>Inicio de Sesión</h1>
<form action="/manejosesiones/login" method="post">
    <div>
        <label for="user">Ingrese el usuario</label>
        <input type="text" id="user" name="user">
    </div>
    <div>
        <label for="password">Ingrese el password</label>
        <input type="password" id="password" name="password">
    </div>
    <div>
        <input type="submit" value="Entrar">
    </div>

    <br><br>

    <a href="/manejosesiones/index.html">Regresar a la Página Principal</a>
</form>
</body>
</html>
