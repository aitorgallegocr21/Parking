/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import utilidadesParking.Funcionamiento;

/**
 *
 * @author usumaniana
 */
public class Parking implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos de instancia
    private Ticket ticket;
    
    // Atributos globales
    private int entradasTotales,
                salidasTotales,
                entradasAbiertas,
                ultimoIDAsignado;
    
    private double importeTotalFacturado,
                   importeEfectivo,
                   importeTarjeta,
                   importeMensual;
    
    // Listas
    private final Map<String, Ticket> ticketsAbiertos;
    final List<Ticket> ticketsCerrados;

    /**
     *
     */
    public Parking() {
        ticketsAbiertos = new HashMap<>();
        ticketsCerrados = new ArrayList<>();   
        entradasTotales = 0;
        salidasTotales = 0;
        entradasAbiertas = 0;
        ultimoIDAsignado = 0;
        importeTotalFacturado = 0.0;
        importeEfectivo = 0.0;
        importeTarjeta = 0.0;
        importeMensual = 0.0;
    }
    
    
    
    // --- MÉTODOS DE GESTIÓN DE PARKING ---

    /**
     *
     * @param salida
     * @param pago */
    public void salir(LocalDateTime salida, FormaPago pago) {
        
        // Se añaden los datos el ticket
        ticket.setFormaPago(pago);
        ticket.setSalida(salida);
        
        // Se calcula el importe del ticket
        ticket.calcularImporte();
        
        if (ticket.getVehiculo().isEsAbonado()) {
            ticket.aplicarDescuento();
        }
        
        // Se actualizan las estadísticas globales
        importeTotalFacturado += ticket.getImporteTotal();
        salidasTotales++;
        entradasAbiertas--;
        
        // Se actualizan las estadísticas de la forma de pago correspondiente
        switch (ticket.getFormaPago()) {
            case EFECTIVO -> importeEfectivo += ticket.getImporteTotal();
            case MENSUAL -> importeMensual += ticket.getImporteTotal();
            case TARJETA -> importeTarjeta += ticket.getImporteTotal();
        }
        
        // Se elimina el ticket abierto y se añade a tickets cerrados
        ticketsAbiertos.remove(ticket.getVehiculo().getMatricula());
        ticketsCerrados.add(ticket);   
    }
    
    /**
     *
     * @return
     */
    public String imprimirTicketEntrada() {
        return "- Número ticket: " + ticket.getNumeroTicket() + "\n"
             + "- Matrícula: " + ticket.getVehiculo().getMatricula() + "\n"
             + "- Propietario: " + ticket.getVehiculo().getNombrePropietario() + "\n"
             + "- Vehículo: " + ticket.getVehiculo().getMarca() + " " + ticket.getVehiculo().getModelo() + " " + ticket.getVehiculo().getColor() + "\n"
             + "- Hora de entrada: " + ticket.getEntrada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm"));
    }
    
    /**
     *
     * @return
     */
    public String imprimirTicketSalida() {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm");
        
        return "- Número ticket: " + ticket.getNumeroTicket() + "\n"
             + "- Matrícula: " + ticket.getVehiculo().getMatricula() + "\n"
             + "- Propietario: " + ticket.getVehiculo().getNombrePropietario() + "\n"
             + "- Vehículo: " + ticket.getVehiculo().getMarca() + " " + ticket.getVehiculo().getModelo() + " " + ticket.getVehiculo().getColor() + "\n"
             + "- Hora de entrada: " + ticket.getEntrada().format(pattern) + "\n"
             + "- Hora de salida: " + ticket.getSalida().format(pattern) + "\n"
             + "- Forma de pago: " + ticket.getFormaPago() + "\n"
             + "- Importe total: " + ticket.getImporteTotal() + "€";    
    }
    
    /**
     *
     * @return
     */
    public String resumenFacturacion() {   
        return    "- Entradas: " + entradasTotales + "\n"
                + "- Salidas: " + salidasTotales + "\n"
                + "- Vehículos dentro del parking: " + entradasAbiertas + "\n"
                + "- Importe total facturado: " + importeTotalFacturado + "€\n"
                + "- Importe por tarjeta: " + importeTarjeta + "€\n"
                + "- Importe por efectivo: " + importeEfectivo + "€\n"
                + "- Importe por abonado mensual: " + importeMensual + "€";     
    }
    
    /**
     *
     * @return
     */
    public String obtenerEstadisticas() {
        
        if (ticketsCerrados.isEmpty()) {
            return "No hay tickets cerrados para mostrar estadísticas";
        }
        
        StringBuilder salida = new StringBuilder();
        
        // Tickets con mayor y menor importe
        Ticket.getTicketMaxImporte(this, salida);
        Ticket.getTicketMinImporte(this, salida);
        
        // Importe por cada método de pago
        Funcionamiento.importePorFormaPago(salida, this);
        
        return salida.toString();
    }

    /**
     *
     * @param matricula
     * @return
     */
    public String buscarTicketsCerradosPorMatricula(String matricula) {
        
        // Lista con la matrícula buscada comparados por fecha de entrada ascendente
        List<Ticket> ticketsCerradosMatricula = ticketsCerrados.stream()
                .filter(t -> t.getVehiculo().getMatricula().equals(matricula))
                .sorted(Comparator.comparing(Ticket::getEntrada).reversed())
                .collect(Collectors.toList());

        if (ticketsCerradosMatricula.isEmpty()) {
            return "No se encontraron tickets cerrados para la matricula " + matricula;
        }
        
        StringBuilder salida = new StringBuilder();

        salida.append("Tickets cerrados con matrícula ")
                .append(matricula)
                .append(": ")
                .append(ticketsCerradosMatricula.size())
                .append("\n");

        ticketsCerradosMatricula.stream().forEach(t -> {
            salida.append(t.toString());
        });
        
        return salida.toString();        
    }
    
    /**
     *
     * @return
     */
    public String listarTicketsCerradosOrdenadosPorImporte() {
        
        if (ticketsCerrados.isEmpty()) {
            return "No se encontraron tickets cerrados.";
        }
        
        // Copia de ticketsCerrados
        List<Ticket> ticketsOrdenadosImporte = new ArrayList<>(ticketsCerrados);
        
        // Se ordena por el orden natural de Comparable<Ticket>
        Collections.sort(ticketsOrdenadosImporte, Comparator.reverseOrder());
        
        // Se construye la salida mapeando por el formato fijo y recolectando en un String
        String salida = "Tickets Cerrados ordenados por Importe -----------\n";
        
        // Defino el contenido del map antes de usar el Stream
        Function<Ticket, String> formatoMap = t -> String.format("%3s", t.getNumeroTicket())
                        + " | "
                        + t.getVehiculo().getMatricula()
                        + " | "
                        + String.format("%8s", t.getImporteTotal())
                        + "€ | "
                        + t.getFormaPago();
        
        salida += "- " + ticketsOrdenadosImporte.stream()
                .map(formatoMap)
                .collect(Collectors.joining("\n- "));
    
        return salida;
    }
    
    /**
     * @param matricula
     * @return
     */
    public boolean anularTicketAbierto(String matricula) {

        var it = ticketsAbiertos.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, Ticket> caja = it.next();

            if (caja.getKey().equals(matricula)) {
                it.remove();
                return true;
            }
        }

        return false;
    }

    /**
     * @return
     */
    public String[] obtenerArrayMatriculas() {

        if (ticketsCerrados.isEmpty()) {
            return new String[0];
        }

        String[] matriculas = ticketsCerrados.stream()
                .map(t -> t.getVehiculo().getMatricula())
                .toArray(String[]::new);

        return matriculas;
    }

    /**
     * @return
     */
    public Double[] obtenerArrayImportes() {

        Double[] importes = ticketsCerrados.stream()
                .map(Ticket::getImporteTotal)
                .toArray(Double[]::new);

        return importes;
    }


    
    // --- GETTERS & SETTERS

    public int getEntradasTotales() {
        return entradasTotales;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Map<String, Ticket> getTicketsAbiertos() {
        return Collections.unmodifiableMap(ticketsAbiertos);
    }

    public List<Ticket> getTicketsCerrados() {
        return Collections.unmodifiableList(ticketsCerrados);
    }
    
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    public void deseleccionarTicket() {
        this.ticket = null;
    }
    
    public void setTicketNuevo(Vehiculo v, LocalDateTime entrada) {
        this.ticket = new Ticket(v, entrada);
        
        ticketsAbiertos.put(ticket.getVehiculo().getMatricula(), ticket);
        entradasTotales++;
        entradasAbiertas++;
        
        // Se le asigna el número de ticket correspondiente
        ultimoIDAsignado++;
        this.ticket.setNumeroTicket(ultimoIDAsignado);
    }

}
