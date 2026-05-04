/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlProducto;

import ModeloProducto.Producto;
import MySql.ConexionMySql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class Control_Producto {

    public List<Producto> obtenerListaProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT idProductos, nombreProductos,"
                + " preciosProductos FROM table_productos";

        try (Connection conn = ConexionMySql.getConnection();
            PreparedStatement pst = conn.prepareStatement(sql); 
            ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt("idProductos"));       // int
                p.setNombre(rs.getString("nombreProductos"));
                p.setPrecio(rs.getDouble("preciosProductos")); // double
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
