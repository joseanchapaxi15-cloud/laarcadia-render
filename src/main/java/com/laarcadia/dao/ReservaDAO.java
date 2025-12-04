package com.laarcadia.dao;

import com.laarcadia.config.DBConnection;
import java.sql.*;

public class ReservaDAO {

    public boolean solapamiento(String fecha, String horaInicio, String horaFin) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservas WHERE fecha = ? AND " +
                     "(hora_inicio < ? AND hora_fin > ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fecha);
            stmt.setString(2, horaFin);
            stmt.setString(3, horaInicio);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (Exception e) {
            throw new SQLException("Error al verificar solapamiento: " + e.getMessage());
        }
    }

    public void insertarReserva(String nombre, String fecha, String horaInicio, String horaFin) throws SQLException {
        String sql = "INSERT INTO reservas (nombre, fecha, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, fecha);
            stmt.setString(3, horaInicio);
            stmt.setString(4, horaFin);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error al insertar reserva: " + e.getMessage());
        }
    }
}