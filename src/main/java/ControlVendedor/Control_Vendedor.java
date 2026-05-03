package ControlVendedor;

import ModeloVendedor.Vendedor;
import MySql.ConexionMySql;
import java.sql.*;

public class Control_Vendedor {

    public Vendedor validarLogin(String nombre, String password) {
        String sql = "SELECT idVendedor, nombreVendedor FROM "
                + "table_vendedor WHERE nombreVendedor = ? "
                + "AND password = ?";
        try (Connection conn = ConexionMySql.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nombre);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Vendedor v = new Vendedor();
                v.setId(rs.getInt("idVendedor"));
                v.setNombre(rs.getString("nombreVendedor"));
                return v;
            }
        } catch (SQLException e) {
            System.out.println("Error en login: " + e.getMessage());
        }
        return null;
    }
}