/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidadesParking;

import java.util.InputMismatchException;
import java.util.Scanner;
import modelo.FormaPago;
import modelo.Parking;
import utilidades.Texto;

/**
 *
 * @author aitorgallegoo_cr
 */
public class LecturaDatos {

    public static int introNumEntPos(Scanner sc) {
        
        int numero;

        while (true) {

            try {
                System.out.print("Introduce un n\u00famero entero: ");
                numero = sc.nextInt();
                sc.nextLine();

            } catch (InputMismatchException e) {
                System.out.println("El n\u00famero introducido debe ser entero.\n");
                sc.nextLine();
                continue;

            }

            if (numero < 0) {
                System.out.println("El n\u00famero debe ser mayor que 0.\n");
                continue;
            }

            break;
        }

        return numero;
    }

    /**
     * Pide introducir una opción. No admite negativos, con decimales u otra cosa que no sea una opción válida.
     * @param sc Scanner para introducir el número.
     * @param min
     * @param max
     * @return El número introducido.
     */
    public static int introOpcion(Scanner sc, int min, int max) {
        
        int numero;
        String mensaje = "Se debe seleccionar una de las opciones disponibles.\n";

        while (true) {
            try {
                System.out.print("Selecciona una opci\u00f3n: ");
                numero = sc.nextInt();
                sc.nextLine();

            } catch (InputMismatchException e) {
                System.out.println(mensaje);
                sc.nextLine();
                continue;
            }

            // Si no es un número de las opciones, se pide de nuevo
            if (numero < min || numero > max) {
                System.out.println(mensaje);
                continue;
            }

            return numero;
        }
    }

    /**
     * @param sc
     * @param dato
     * @param parking
     * @param validacion
     * @return
     */
    public static String introDatoVehiculo(Scanner sc, String dato, Parking parking, boolean validacion) {
        
        String entradaDato;
        dato = Formateador.formatearNombre(dato);
        
        while (true) {
            
            System.out.print("Introduce el/la " + dato + " del veh\u00edculo: ");
            
            try {
                entradaDato = sc.nextLine();

                if (validacion) {
                    Validacion.validarDato(dato, entradaDato, parking);
                }
            
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + "\n");
                continue;
            }

            if (!Texto.formatearTexto(dato).equals("matricula")) {
                entradaDato = Formateador.formatearNombre(entradaDato); // Datos (no matrícula)
            
            } else {
                entradaDato = entradaDato.toUpperCase(); // Matrícula
            }

            System.out.println(dato + " introducido/a correctamente: " + entradaDato + "\n");
            return entradaDato;
        }
    }

    /**
     * Solicita el color del vehículo asegurando que el campo no quede vacío.
     * <p>
     * Valida que la entrada no esté en blanco, aplica un formato de texto normalizado
     * (primera letra en mayúscula) y confirma la operación por pantalla.
     * </p>
     *
     * @param sc Scanner para la entrada del usuario.
     * @return El color del vehículo correctamente formateado.
     */
    public static boolean esAbonado(Scanner sc) {
        
        // Bucle para introducir si tiene cama supletoria o no
        while (true) {

            System.out.print("\u00bfEl veh\u00edculo es abonado? (S/N): ");
            String respuesta = sc.nextLine().trim().toUpperCase();

            if (!respuesta.equals("S") && !respuesta.equals("N")) {
                System.out.println("Debes introducir S o N (Si o No).");
                continue;
            }

            // Si la respuesta es S, es true, si no, false.
            return respuesta.equals("S");
        }
    }

    /**
     * Gestiona la selección manual del método de pago a través de un menú numérico.
     * <p>
     * Presenta las opciones disponibles (Tarjeta, Efectivo) y valida que la entrada
     * sea un número entero correspondiente a una de las opciones del enum {@link FormaPago}.
     * </p>
     *
     * @param sc Scanner para la lectura de la opción seleccionada.
     * @return La instancia de {@link FormaPago} elegida por el usuario.
     */
    public static FormaPago introPago(Scanner sc) {
        
        System.out.println("Tarjeta (1) - Efectivo (2)");
        int opcion;

        while (true) {

            System.out.print("Introduce el m\u00e9todo de pago: ");
            
            try {
                opcion = sc.nextInt();
                sc.nextLine();

            } catch (InputMismatchException e) {
                System.out.println("El n\u00famero debe ser una de las opciones.\n");
                sc.nextLine();
                continue;
            }
            
            switch (opcion) {
                case 1 -> {
                    return FormaPago.TARJETA;
                }
                case 2 -> {
                    return FormaPago.EFECTIVO;
                }
                default -> System.out.println("Se debe seleccionar una de las opciones.\n");
            }
        }
    }
    
}
