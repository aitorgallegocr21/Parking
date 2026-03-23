/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplicacion;

import java.time.LocalDateTime;
import java.util.Scanner;
import modelo.FormaPago;
import modelo.Parking;
import modelo.Vehiculo;
import utilidadesParking.Funcionamiento;
import utilidadesParking.LecturaDatos;
import utilidadesParking.Validacion;

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
        
        String matricula = LecturaDatos.introDatoVehiculo(sc, "Matrícula", parking);
        
        System.out.println(parking.buscarTicketsCerradosPorMatricula(matricula));
    }
 
    /**
     *
     */
    public void ticketsCerradosPorImporte() {
        System.out.println(parking.listarTicketsCerradosOrdenadosPorImporte());
    }
    
}
