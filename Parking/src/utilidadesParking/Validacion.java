/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidadesParking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import modelo.Parking;

/**
 *
 * @author aitorgallegoo_cr
 */
public class Validacion {

    /**
     * Pide una fecha y hora con un determinado formato.
     * @param sc Scanner para introducir lo que se pida.
     * @return La fecha y hora introducida.
     */
    public static LocalDateTime leerFechaHora(Scanner sc) {
        
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime fechaYHora;
        String input;
        
        // Bucle para introducir los datos que se rompe cuando es correcto.
        while (true) {
            System.out.print("Introduce la fecha y hora (dd/MM/yyyy HH:mm): ");
            try {
                input = sc.nextLine().trim();
                fechaYHora = LocalDateTime.parse(input, formato);
                
            } catch (Exception e) {
                System.out.println("El formato introducido no es v\u00e1lido.\n");
                continue;
            }
            break;
        }
        return fechaYHora;
    }

    /**
     * Valida la fecha de salida.
     * @param entrada Fecha de entrada.
     * @param salida Fecha de salida.
     * @throws IllegalArgumentException La suelta si no es válido.
     */
    public static void validarSalida(LocalDateTime entrada, LocalDateTime salida) throws IllegalArgumentException {
        if (salida.isBefore(entrada)) {
            throw new IllegalArgumentException("La salida debe ser posterior a la entrada.");
        }
    }

    /**
     * @param entradaDato
     * @param dato
     * @throws IllegalArgumentException
     */
    public static void validarDatosVehiculo(String entradaDato, String dato) throws IllegalArgumentException {
        if (entradaDato.isBlank() || entradaDato.isEmpty()) {
            throw new IllegalArgumentException("No puede estar vac\u00edo.");

        // Regex nombres completos en español
        } else if (!entradaDato.matches("^[\\p{L}]+(?:[\\s-][\\p{L}]+)*$")) {
            throw new IllegalArgumentException("No puede contener caracteres impropios de un/una " + dato);
        }
    }

    /**
     * @param matricula
     * @param parking
     * @throws IllegalArgumentException
     */
    public static void validarMatricula(String matricula, Parking parking) throws IllegalArgumentException {
        
        if (matricula.isEmpty()) {
            throw new IllegalArgumentException("La matr\u00edcula no puede estar vac\u00eda.");

        } else if (matricula.contains(" ")) {
            throw new IllegalArgumentException("La matr\u00edcula no debe contener espacios intermedios.");

        } else if (matricula.length() != 7 && matricula.length() != 6) {
            throw new IllegalArgumentException("Longitud incorrecta. La matr\u00edcula debe tener 6 o 7 caracteres.");

        } else if (!matricula.matches("^(\\d{4}[BCDFGHJKLMNPRSTVWXYZ]{3}|[BCDFGHJKLMNPRSTVWXYZ]{3}\\d{4})$")) {
            throw new IllegalArgumentException("Formato no v\u00e1lido. Solo se permiten letras y n\u00fameros y el formato Espa\u00f1ol moderno.");
        }
        
        // Valida si ya existe la matrícula dentro del Parking
        if (parking.getTicketsAbiertos() != null) {
            if (parking.getTicketsAbiertos().containsKey(matricula)) {
                throw new IllegalArgumentException("Esta matr\u00edcula se encuentra registrada dentro del Parking.");
            }
        }
    }

    /**
     * @param dato
     * @param entradaDato
     * @param parking
     * @throws IllegalArgumentException
     */
    public static void validarDato(String dato, String entradaDato, Parking parking) throws IllegalArgumentException {
        
        dato = Formateador.formatearTexto(dato).toLowerCase();
        
        try {
            if (dato.equals("matricula")) {
                validarMatricula(entradaDato.toUpperCase(), parking);
            
            } else if (dato.equals("marca") || dato.equals("modelo") || dato.equals("color") || dato.equals("propietario/a")) {
                validarDatosVehiculo(entradaDato, dato);
            }
        
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    
    
    
}
