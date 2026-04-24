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
public class SumarMedianteTeclado {
    
    public static void main(String[] args) {
        // 1. Crear el objeto Scanner para leer desde teclado
        Scanner leer = new Scanner(System.in);
        // 2. Pedir el primer número
        
        System.out.print("Ingresa el primer número: ");
        int numero1 = leer.nextInt();

        // 3. Pedir el segundo número
        System.out.print("Ingresa el segundo número: ");
        int numero2 = leer.nextInt();

        // 4. Calcular la suma
        int suma = numero1 + numero2;

        // 5. Mostrar el resultado
        System.out.println("La suma de " + numero1 + " + " + numero2 + " es: " + suma);
                // 6. Cerrar el Scanner (buena práctica)
        leer.close();
    }
}
