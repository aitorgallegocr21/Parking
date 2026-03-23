/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package aplicacion;

import java.util.Scanner;

/**
 *
 * @author usumaniana
 */
public class AplicacionParking {
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
        // Objetos de Parking, gestión de App y Scanner
        final var parking = GestionDatos.cargarParking();
        final var sc = new Scanner(System.in);
        final var gestor = new GestionApp(parking, sc);
        
        gestor.accion();

        // Guardar datos de parking
        GestionDatos.guardarParking(parking);
        
        System.out.println("\n--- PROGRAMA FINALIZADO ---\n");
        sc.close();
    }
     
}
