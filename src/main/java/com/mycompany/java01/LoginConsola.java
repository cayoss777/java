/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.java01;

import java.util.Scanner;

/**
 *
 * @author USUARIO
 */
public class LoginConsola {
    

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        
        // Credenciales válidas (puedes cambiarlas)
        String usuarioValido = "root";
        String passwordValido = "UAI";
        
        String usuario;
        String password;
        
        System.out.println("=== SISTEMA DE LOGIN ===\n");
        
        while (true) {
            // Pedir usuario
            System.out.print("Ingrese usuario: ");
            usuario = leer.nextLine();
            
            // Pedir contraseña
            System.out.print("Ingrese contraseña: ");
            password = leer.nextLine();
            
            // Verificar credenciales
            if (usuario.equals(usuarioValido) && password.equals(passwordValido)) {
                System.out.println("\n✅ ¡Login exitoso! Bienvenido " + usuario);
                break;  // Sale del bucle
            } else {
                System.out.println("\n❌ Usuario o contraseña incorrectos. Intente nuevamente.\n");
            }
        }
        
        leer.close();
    }
}

