/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.uai2026;

import VistaCliente.frmPrincipal;
import VistaVendedor.frmLoginGUI;
import javax.swing.SwingUtilities;

/**
 *
 * @author USUARIO
 */
public class Uai2026 {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        // Usando la buena práctica con SwingUtilities
        SwingUtilities.invokeLater(() -> {
            // Suponiendo que tienes una clase llamada frmLogin
            frmLoginGUI inicioLogin = new frmLoginGUI();
            inicioLogin.setVisible(true);
        });
    }
}
