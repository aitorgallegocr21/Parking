/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplicacion;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import modelo.FormaPago;
import modelo.Parking;
import modelo.Vehiculo;

import utilidadesParking.*;;

/**
 *
 * @author aitorgallegoo_cr
 */
public class GestionApp {

    private final Parking parking;
    private final Scanner sc;
    
    /**
     *
     * @param parking
     * @param sc
     */
    public GestionApp(Parking parking, Scanner sc) {
        this.parking = parking;
        this.sc = sc;
    }
    
    
    
    // --- MÉTODOS DE FUNCIONAMIENTO DEL PROGRAMA PRINCIPAL ---
    
    /**
     * 
     */
    public void accion() {
        
        while (true) {

            int opcion = seleccionarAccion();
            
            switch (opcion) {
                case 1 -> registrarEntradaVehiculo();  
                case 2 -> registrarSalidaVehiculo();
                case 3 -> System.out.println(parking.resumenFacturacion() + "\n");
                case 4 -> listarTicketAbiertos();
                case 5 -> listarTicketCerrados();
                case 6 -> statsTicketsCerrados();
                case 7 -> buscarTCerradosPorMat();
                case 8 -> ticketsCerradosPorImporte();
                case 9 -> anularTicketAbiertoDeParking();
                case 10 -> vehiculosRecurrentes();
                case 11 -> topNTicketsMasCaros();
                case 12 -> { return; }
            }
        }
    }
    
    /**
     *
     */
    public void registrarSalidaVehiculo() {
        
        // Seleccionar vehículo que va a salir
        Funcionamiento.seleccionarTicket(parking, sc);
        
        if (parking.getTicket() == null) {
            return;
        }
        
        LocalDateTime salida = Funcionamiento.salida(parking, sc);
        FormaPago pago = Funcionamiento.pago(parking, sc);
        
        parking.salir(salida, pago);
        
        System.out.println(parking.imprimirTicketSalida() + "\n");
        
        parking.deseleccionarTicket();
    }

    /**
     *
     * @return
     */
    public int seleccionarAccion() {
        
        System.out.println("\n--- PARKING ---");
        System.out.println(Funcionamiento.menu() + "\n");
        
        int opcion = LecturaDatos.introOpcion(sc, 1, 12);
        
        System.out.println("");
        
        return opcion;
    }

    /**
     *
     */
    public void registrarEntradaVehiculo() {
        
        // Se crea el vehículo con los datos introducidos.
        Vehiculo v1 = Funcionamiento.vehiculo(sc, parking);
        
        System.out.println("\nEntrada de veh\u00edculo en el parking:");
        
        // Se pide la fecha de entrada del vehículo al parking
        LocalDateTime entrada = Validacion.leerFechaHora(sc);
        
        // Se guarda el nuevo ticket
        parking.setTicketNuevo(v1, entrada);
        
        // Se muestra el ticket de entrada
        System.out.println("\n" + parking.imprimirTicketEntrada() + "\n");
    }
    
    /**
     *
     */
    public void listarTicketAbiertos() {
        
        System.out.println("Ticket/s Abiertos (" + parking.getTicketsAbiertos().size() + " ticket/s) ----------------------");
        
        parking.getTicketsAbiertos().entrySet().forEach(entry -> {
            System.out.println("- " + entry.getValue().toStringBasico());
        });
    }
    
    /**
     *
     */
    public void listarTicketCerrados() {
        
        System.out.println("Ticket/s Cerrados (" + parking.getTicketsCerrados().size() + " ticket/s) ----------------------");
        
        parking.getTicketsCerrados().forEach(t -> {
            System.out.println("- " + t);
        });
        
    }
    
    /**
     *
     */
    public void statsTicketsCerrados() {
        System.out.println("Estadísticas de tickets cerrados --------------------");
        System.out.println(parking.obtenerEstadisticas());
    }
    
    /**
     *
     */
    public void buscarTCerradosPorMat() {
        
        String matricula = LecturaDatos.introDatoVehiculo(
                sc,
                "Matrícula",
                parking,
                true
        );
        
        System.out.println(parking.buscarTicketsCerradosPorMatricula(matricula));
    }

    /**
     *
     */
    public void ticketsCerradosPorImporte() {
        System.out.println(parking.listarTicketsCerradosOrdenadosPorImporte());
    }
    
    /**
     * 
     */
    public void anularTicketAbiertoDeParking() {

        // Se introduce matrícula sin validación para que permita introducir matrícula antes introducida
        String matricula = LecturaDatos.introDatoVehiculo(
                sc,
                "Matrícula",
                parking,
                false
        );

        if (parking.anularTicketAbierto(matricula)) {
            System.out.println("Ticket " + matricula + " eliminado correctamente.");

        } else {
            System.out.println("No se encontró ningún ticket con matrícula " + matricula + ".");
        }
    }

    /**
     * 
     */
    public void vehiculosRecurrentes() {

        String[] matriculas = parking.obtenerArrayMatriculas();

        Arrays.sort(matriculas);

        // Se filtran por matrículas las cuales aparecen más de 1 vez
        String[] recurrentes = Arrays.stream(matriculas)
                .filter(m -> (
                        Arrays.stream(matriculas) // Segundo stream para contar
                                .filter(mat -> mat.equals(m))
                                .count() >= 2
                ))
                .distinct() // Para que las matrículas recurrentes solo salgan una vez
                .toArray(String[]::new);

        if (matriculas.length == 0) {
            System.out.println("No se han detectado vehículos.");
            return;
        }

        if (recurrentes.length == 0) {
            System.out.println("No se han detectado vehículos recurrentes.");
            return;
        }

        // Mostrar el array ordenado y luego el de vehículos recurrentes
        System.out.println("Lista de matrículas ordenadas ----------------------------------\n");
        System.out.println(Arrays.toString(matriculas));
        System.out.println();
        System.out.println("Lista de vehículos recurrentes (2 o más veces) -----------------\n");
        System.out.println(Arrays.toString(matriculas));
    }

    /**
     * 
     */
    public void topNTicketsMasCaros() {

        // Se obtiene el array de importes
        Double[] importes = parking.obtenerArrayImportes();

        if (importes.length == 0) {
            System.out.println("No hay tickets cerrados.");
            return;
        }

        Arrays.sort(importes, Comparator.reverseOrder());

        int cantidad = LecturaDatos.introOpcion(sc, 1, Integer.MAX_VALUE);

        // Se valida la cantidad elegida
        if (cantidad > importes.length) {
            System.out.println("No hay tantos tickets cerrados.");
            cantidad = importes.length;
        }

        Double[] importesCantidad = Arrays.copyOfRange(importes, 0, cantidad);

        System.out.println("Ranking de " + cantidad + " ticket/s más caros --------------------");
        System.out.println(Arrays.toString(importesCantidad));
    }

}
