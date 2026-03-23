/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplicacion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import modelo.Parking;

/**
 *
 * @author aitorgallegoo_cr
 */
public class GestionDatos implements Serializable {

    private static final String ARCHIVO_DATOS = "parking.dat";
    
    public static Parking cargarParking() {
        
        File archivo = new File(ARCHIVO_DATOS);
        
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                System.out.println("Estado del parking cargado correctamente.");
                return (Parking) ois.readObject();
            
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar datos. Se creará un parking nuevo.");
            }
        }
        
        return new Parking(); // Si no existe o falla, devuelve uno nuevo
    }

    public static void guardarParking(Parking parking) {
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(parking);
            System.out.println("Estado del parking guardado correctamente.");
        
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }
    
}
