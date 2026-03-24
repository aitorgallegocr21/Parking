/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidadesParking;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import modelo.FormaPago;
import modelo.Parking;
import modelo.Ticket;
import modelo.Vehiculo;


/**
 *
 * @author usumaniana
 */
public class Funcionamiento {
    
    /**
     * Muestra el menú del programa.
     * @return La cadena de texto con el menú.
     */
    public static String menu() {
        return """
               1 - Registrar entrada de veh\u00edculo
               2 - Registrar salida de veh\u00edculo
               3 - Mostrar resumen de facturaci\u00f3n
               4 - Listar tickets abiertos
               5 - Listar tickets cerrados
               6 - Estadísticas de tickets cerrados
               7 - Buscar tickets cerrados por matrícula
               8 - Listar tickets cerrados por importe
               9 - Anular ticket abierto
               10 - Vehículos recurrentes
               11 - Ranking de los tickets más caros
               12 - Salir""";  
    }
    
    /**
     *
     * @param parking
     * @param sc
     */
    public static void seleccionarTicket(Parking parking, Scanner sc) {
        
        if (parking.getTicketsAbiertos().isEmpty()) {
            System.out.println("No hay ningún vehículo dentro del parking. debes registrar al menos uno.\n");
            return;
        }

        System.out.println("Tickets abiertos disponibles");

        // Se convierten los valores del mapa a una lista para poder usar números a la hora de elegir
        var listaAbiertos = new ArrayList<>(parking.getTicketsAbiertos().values());

        // Se selecciona el ticket de la lista
        parking.setTicket(
                elegirDeLista(
                        sc,
                        listaAbiertos,
                        t -> t.getVehiculo().getMatricula(),
                        "Selecciona el vehículo")
        );
    }
    
    /**
     * Muestra una lista de elementos numerados y permite al usuario seleccionar
     * uno, personalizando la visualización de cada objeto mediante un
     * transformador (lambda).
     * <p>
     * A diferencia de la versión básica, este método permite decidir qué
     * información mostrar de cada objeto (por ejemplo, solo la matrícula de un
     * Vehículo) sin necesidad de modificar el método {@code toString()} de la
     * clase original.
     * </p>
     *
     * @param <T> Tipo genérico de los elementos contenidos en la lista.
     * @param sc Objeto {@link Scanner} para leer la entrada del usuario.
     * @param lista La {@link List} de objetos que se mostrará al usuario.
     * @param mensaje Texto descriptivo para solicitar la elección.
     * @param transformador Función lambda que define qué parte del objeto
     * mostrar como texto.
     * @return El objeto de tipo {@code T} seleccionado, o {@code null} si la
     * lista está vacía.
     */
    public static <T> T elegirDeLista(Scanner sc, List<T> lista, Function<T, String> transformador, String mensaje) {
        
        if (lista == null || lista.isEmpty()) {
            return null;
        }

        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("[%d] %s%n", (i + 1), transformador.apply(lista.get(i)));
        }
        
        System.out.print("\n" + mensaje + " (1-" + lista.size() + "): ");
        int eleccion = LecturaDatos.introOpcion(sc, 1, lista.size(), "Selecciona un ticket: ");

        return lista.get(eleccion - 1);
    }
    
    /**
     *
     * @param parking
     * @param sc
     * @return
     */
    public static LocalDateTime salida(Parking parking, Scanner sc) {
        
        LocalDateTime salida;
        var formatoFechaHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.println("\nSalida del vehículo del parking --------------------------");
        
        while (true) {
            
            try {
                salida = Validacion.leerFechaHora(sc);
                Validacion.validarSalida(parking.getTicket().getEntrada(), salida);
                break;
                
            } catch (IllegalArgumentException e) {
                System.out.println("La fecha de salida debe ser posterior a la fecha de entrada ("
                        + parking.getTicket().getEntrada().format(formatoFechaHora)
                        + ").\n");
            }
        }
        
        System.out.println("Fecha y hora introducidas correctamente: " + salida.format(formatoFechaHora) + "\n");
        return salida;
    }

    /**
     *
     * @param parking
     * @param sc
     * @return
     */
    public static FormaPago pago(Parking parking, Scanner sc) {
        // Si el vehículo no es abonado, se pide método de pago
        if (!parking.getTicket().getVehiculo().isEsAbonado()) {
            return LecturaDatos.introPago(sc);

        } else {
            return FormaPago.MENSUAL;
        }
    }
    
    /**
     *
     * @param sc
     * @param parking
     * @return
     */
    public static Vehiculo vehiculo(Scanner sc, Parking parking) {
        
        String matricula = LecturaDatos.introDatoVehiculo(sc, "Matrícula", parking, true);
        String nombrePro = LecturaDatos.introDatoVehiculo(sc, "Propietario/a", parking, true);
        String marca = LecturaDatos.introDatoVehiculo(sc, "Marca", parking, true);
        String modelo = LecturaDatos.introDatoVehiculo(sc, "Modelo", parking, true);
        String color = LecturaDatos.introDatoVehiculo(sc, "Color", parking, true);
        boolean abonado = LecturaDatos.esAbonado(sc);
        
        return new Vehiculo(matricula, nombrePro, marca, modelo, color, abonado);
    }

    /**
     * 
     * @param salida
     * @param parking
     */
    public static void importePorFormaPago(StringBuilder salida, Parking parking) {
        
        // --- Tickets pagados por cada forma de pago ---
        var listaFormasPago = parking.getTicketsCerrados().stream()
                .map(Ticket::getFormaPago)
                .toList();
        
        listaFormasPago.stream().distinct().forEach(f -> {
                salida.append("- ")
                        .append("Tickets pagados con forma de pago ")
                        .append(Formateador.formatearNombre(f.toString()))
                        .append(": ")
                        .append(Collections.frequency(listaFormasPago, f));
        });
    }
    
}