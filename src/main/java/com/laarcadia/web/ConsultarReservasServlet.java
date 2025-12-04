package com.laarcadia.web;

import com.laarcadia.config.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ConsultarReservas")
public class ConsultarReservasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        try (PrintWriter out = resp.getWriter();
             Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT nombre, fecha, hora_inicio, hora_fin FROM reservas ORDER BY fecha, hora_inicio");
             ResultSet rs = ps.executeQuery()) {

            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            while (rs.next()) {
                if (!first) json.append(",");
                json.append("{")
                    .append("\"nombre\":\"").append(escape(rs.getString("nombre"))).append("\",")
                    .append("\"fecha\":\"").append(rs.getString("fecha")).append("\",")
                    .append("\"horaInicio\":\"").append(rs.getString("hora_inicio")).append("\",")
                    .append("\"horaFin\":\"").append(rs.getString("hora_fin")).append("\"")
                    .append("}");
                first = false;
            }
            json.append("]");
            out.print(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().print("[]");
        }
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}