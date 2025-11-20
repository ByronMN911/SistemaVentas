package controllers;

/*
 * Autor: Byron Melo
 * Fecha: 13/11/2025
 * Version: 1.0
 * Descripcion: Servlet encargado de generar la factura de compra en formato PDF.
 * Utiliza la librería iText 5 para crear el documento dinámicamente
 * basándose en los productos que el usuario tiene en su sesión (Carro).
 */

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import models.DetalleCarro;
import models.ItemCarro;

import java.io.IOException;

@WebServlet("/descargar-factura")
public class DescargarFacturaServlet extends HttpServlet {

    /*
     * Este metodo maneja la petición GET cuando el usuario accede a /descargar-factura.
     * Obtiene el carro desde la sesión y genera un archivo PDF con los datos de la compra.
     * También configura los headers HTTP para que el navegador descargue el archivo.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtener la sesión actual del usuario
        HttpSession session = req.getSession();

        // Recuperar el atributo "carro", que contiene los productos seleccionados
        DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");

        // 2. Validación: si el carro es nulo o no tiene ítems, evitamos generar un PDF vacío
        if (detalleCarro == null || detalleCarro.getItem().isEmpty()) {
            // Redirigimos al usuario nuevamente al carro
            resp.sendRedirect(req.getContextPath() + "/ver-carro");
            return;
        }

        // 3. Configurar el tipo de contenido como PDF para la respuesta HTTP
        resp.setContentType("application/pdf");

        // Cabecera para forzar al navegador a descargar el archivo
        resp.setHeader("Content-Disposition", "attachment; filename=factura_compra.pdf");

        try {
            // 4. Crear el documento PDF usando la clase Document de iText
            Document documento = new Document();

            // Vincular el documento con el OutputStream de la respuesta (envío directo al navegador)
            PdfWriter.getInstance(documento, resp.getOutputStream());

            // Abrimos el documento para comenzar a escribir contenido
            documento.open();

            // 5. Crear el título del PDF
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLUE);
            Paragraph titulo = new Paragraph("Factura de Compra", fontTitulo);

            // Centrar el título
            titulo.setAlignment(Element.ALIGN_CENTER);

            // Agregar título al documento
            documento.add(titulo);

            // Línea en blanco para separación visual
            documento.add(new Paragraph(" "));

            // 6. Crear la tabla de productos con 5 columnas (ID, nombre, precio, cantidad, subtotal)
            PdfPTable tabla = new PdfPTable(5);

            // Hacer que la tabla ocupe todo el ancho del documento
            tabla.setWidthPercentage(100);

            // 6a. Generar encabezados con estilo
            String[] headers = {"ID", "Producto", "Precio", "Cant.", "Subtotal"};

            for (String header : headers) {

                // Crear celda para cada encabezado usando una fuente en negrita
                PdfPCell cell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

                // Color de fondo gris para distinguir los encabezados
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

                // Alinear texto al centro
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                // Agregar celda a la tabla
                tabla.addCell(cell);
            }

            // 7. Recorrer la lista de productos y agregar cada uno como una fila en la tabla
            for (ItemCarro item : detalleCarro.getItem()) {

                // Columna ID del producto
                tabla.addCell(item.getProducto().getIdProducto().toString());

                // Columna Nombre del producto
                tabla.addCell(item.getProducto().getNombre());

                // Columna Precio formateado con 2 decimales
                tabla.addCell("$" + String.format("%.2f", item.getProducto().getPrecio()));

                // Columna Cantidad del producto
                tabla.addCell(String.valueOf(item.getCantidad()));

                // Columna Subtotal del producto (precio * cantidad)
                tabla.addCell("$" + String.format("%.2f", item.getSubtotal()));
            }

            // Agregar la tabla completa al documento PDF
            documento.add(tabla);

            // 8. Crear sección de totales al final del documento (Subtotal, IVA, Total)
            Paragraph totales = new Paragraph();

            // Alinear totales a la derecha del documento
            totales.setAlignment(Element.ALIGN_RIGHT);

            // Añadir un espacio antes de los totales
            totales.setSpacingBefore(20);

            // Subtotal general de la compra
            totales.add(new Chunk("Subtotal: $" + String.format("%.2f", detalleCarro.getSubtotal()) + "\n"));

            // IVA calculado (15% en tu modelo)
            totales.add(new Chunk("IVA (15%): $" + String.format("%.2f", detalleCarro.getSubtotalIva()) + "\n"));

            // Total final con formato en negrita
            Font fontTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            totales.add(new Chunk("Total a Pagar: $" + String.format("%.2f", detalleCarro.getTotal()), fontTotal));

            // Agregar totales al documento
            documento.add(totales);

            // 9. Cerrar documento para completar la generación del PDF
            documento.close();

        } catch (DocumentException e) {
            // Si ocurre un error relacionado con iText, lo envolvemos en IOException
            throw new IOException(e);
        }
    }
}
