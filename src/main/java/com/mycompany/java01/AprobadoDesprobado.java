/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.java01;

import java.util.Scanner;

/**
 *
 * @author USUARIO
 */
public class AprobadoDesprobado {

    public static void main(String[] args) {
        //System.out.println("Hello World!");
     
        Scanner leer = new Scanner(System.in);
        
        // 1. Solicitar las 3 notas
        System.out.print("Ingrese la primera nota: ");
        double nota1 = leer.nextDouble();
        
        System.out.print("Ingrese la segunda nota: ");
        double nota2 = leer.nextDouble();
        
        System.out.print("Ingrese la tercera nota: ");
        double nota3 = leer.nextDouble();
        
        // 2. Calcular el promedio
        double promedio = (nota1 + nota2 + nota3) / 3;
        
        // 3. Mostrar el promedio (opcional)
        System.out.println("Promedio: " + promedio);
        
        // 4. Determinar si aprueba o desaprueba
        if (promedio >= 11) {
            System.out.println("¡APRUEBA! (Promedio: " + promedio + ")");
        } else {
            System.out.println("DESAPRUEBA. (Promedio: " + promedio + ")");
        }
        
        leer.close();
   
    }
}
