/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.uai2026;

/**
 *
 * @author USUARIO
 */
// Importa las clases necesarias al inicio del archivo
import java.sql.*;

public class PruebaDriver {
    public static void main(String[] args) {
        try {
            // Esta línea intenta cargar el driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("¡Driver de MySQL (com.mysql.cj.jdbc.Driver) cargado exitosamente!");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: El Driver 'com.mysql.cj.jdbc.Driver' no se encuentra.");
            System.out.println("Verifica que el conector JAR de MySQL esté en tu proyecto.");
        }
    }
}