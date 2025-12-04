package com.laarcadia.web;

import com.laarcadia.dao.ReservaDAO;
import com.laarcadia.notify.Notificador;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/Reservar")
public class ReservarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String nombre = req.getParameter("nombre");
        String fecha = req.getParameter("fecha");
        String horaInicio = req.getParameter("horaInicio");
        String horaFin = req.getParameter("horaFin");
        String telefono = req.getParameter("telefono"); // nuevo campo

        try {
            ReservaDAO dao = new ReservaDAO();

            // Validar solapamiento
            boolean existe = dao.solapamiento(fecha, horaInicio, horaFin);
            if (existe) {
                out.print("⚠️ La cancha ya está reservada en ese horario.");
                return;
            }

            // Insertar reserva
            dao.insertarReserva(nombre, fecha, horaInicio, horaFin);
            out.print("✅ Reserva registrada con éxito.");

            // Notificación por WhatsApp al administrador (incluye número del cliente)
            String mensajeAdmin = "📢 Nueva reserva: " + nombre +
                                  " el " + fecha +
                                  " de " + horaInicio +
                                  " a " + horaFin +
                                  "\n📞 Número del cliente: " + telefono;
            Notificador.enviarWhatsApp(mensajeAdmin);

            // Confirmación al cliente
            String mensajeCliente = "✅ Hola " + nombre +
                                    ", tu reserva está confirmada para el " + fecha +
                                    " de " + horaInicio + " a " + horaFin +
                                    ". ¡Gracias por elegir La Arcadia!";
            Notificador.enviarWhatsAppCliente(telefono, mensajeCliente);

        } catch (SQLException e) {
            e.printStackTrace();
            out.print("❌ Error al registrar la reserva: " + e.getMessage());
        }
    }
}