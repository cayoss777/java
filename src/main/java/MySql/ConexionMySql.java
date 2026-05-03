/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MySql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author USUARIO
 */
public class ConexionMySql {
    //private static final String URL = "jdbc:mysql://localhost:3306/bd_dos";
    //Agregar parámetros a la URL (evita advertencias de zona horaria y SSL) d 
    private static final String URL = "jdbc:mysql://localhost:3306/bd_dos?useSSL=false&serverTimezone=UTC";
    private static final String USER = "Claudios";
    private static final String PASS = "Claudios";

    // Bloque estático para registrar el driver (opcional en Java 8+)
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
