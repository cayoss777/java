
package com.mycompany.mavenpoo;

import java.util.Scanner;


public class MavenPOO {

    public static void main(String[] args) {
        System.out.println("Hello Programador!");
        
        Scanner sc= new Scanner(System.in);
        
        String usuarioCorrecto="claudio";
        String contrasenaCorrecta="huancahuire";
        
        System.out.println("Login");
        System.out.println("Ingrese usuario :");
        String usuario = sc.nextLine();
        
        System.out.println("Ingrese contraseña ");
        String contrasena = sc.nextLine();
        
        //Validar
        if(usuario.equals(usuarioCorrecto) && contrasena.equals(contrasenaCorrecta)) {
            System.out.println("Bienvenido al sistema");
        }else{
            System.out.println("Ud. no es usuario");
        }
        sc.close();
    }
}
