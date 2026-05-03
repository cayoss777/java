package ControladorClientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ModeloCliente.Cliente;   // Ajusta el paquete
import ModeloCliente.Cliente;
import MySql.ConexionMySql;

public class Control_Cliente {

    public List<Cliente> obtenerListaClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT idCliente, Nombre_Cliente,"
                + " Apellido_Cliente, Cedula_Cliente FROM table_cliente";

        try (Connection conn = ConexionMySql.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("idCliente"));
                c.setNombre(rs.getString("Nombre_Cliente"));
                c.setApellido(rs.getString("Apellido_Cliente"));
                c.setCedula(rs.getString("Cedula_Cliente"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}