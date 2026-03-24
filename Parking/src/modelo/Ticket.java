/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import utilidadesParking.Formateador;

/**
 *
 * @author usumaniana
 */
public class Ticket implements Comparable<Ticket>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Atributos de instancia
    private int numeroTicket;
    private Vehiculo vehiculo;
    private LocalDateTime entrada;
    private LocalDateTime salida;
    private FormaPago formaPago;
    private double importeTotal;
    
    // Constantes de configuración
    private static final double     PRECIO_MINIMO                = 2d,
                                    PRECIO_MINUTO                = 0.015,
                                    PRECIO_DIA                   = 19d,
                                    PRECIO_SEMANA                = 80d,
                                    DESCUENTO                    = 0.1d;
    
    private static final int        LIMITE_MIN_PRECIO_MINIMO     = 90,
                                    MINUTOS_DIA                  = 1440,
                                    DIAS_SEMANA                  = 7,
                                    MINUTOS_SEMANA               = MINUTOS_DIA * DIAS_SEMANA;
    
    
    
    public Ticket(Vehiculo vehiculo, LocalDateTime entrada) {  
        this.vehiculo = vehiculo;
        this.entrada = entrada;
    }
    
    
    
    /**
     * 
     */
    public void aplicarDescuento() {
        importeTotal -= importeTotal * DESCUENTO;
        importeTotal = Formateador.redondear(importeTotal);
    }

    /**
     * Realiza el desglose matemático y calcula el coste total de la estancia.
     * <p>
     * El cálculo se basa en la diferencia temporal entre la entrada y la salida, 
     * aplicando una jerarquía de tarifas y reglas de negocio:
     * <ul>
     * <li><b>Precio Mínimo:</b> Si la estancia es inferior a 90 minutos ({@code LIMITE_MIN_PRECIO_MINIMO}), 
     * se aplica una tarifa fija de 2€ ({@code PRECIO_MINIMO}).</li>
     * <li><b>Tarifa por Minutos:</b> Para estancias menores a un día, se factura por cada 
     * minuto transcurrido a razón de {@code PRECIO_MINUTO}.</li>
     * <li><b>Tarifa Diaria:</b> Si se supera el día, se contabilizan los días completos 
     * a {@code PRECIO_DIA} más los minutos restantes.</li>
     * <li><b>Tarifa Semanal y Descuento:</b> Para estancias de una semana o más, se factura 
     * por semanas ({@code PRECIO_SEMANA}), días y minutos. Al total obtenido se le 
     * aplica un descuento por larga estancia del 10% ({@code DESCUENTO}).</li>
     * </ul>
     * </p>
     * El resultado final se asigna al atributo de instancia {@code importeTotal}.
     */
    public void calcularImporte() {
        
        // Se obtiene el total de minutos una sola vez
        long totalMinutos = Duration.between(entrada, salida).toMinutes();

        // Caso especial: Estancia muy corta (menos de 90 min)
        if (totalMinutos < LIMITE_MIN_PRECIO_MINIMO) {
            importeTotal = PRECIO_MINIMO;
            return;
        }

        // Desglose matemático (Semanas -> Días -> Minutos restantes)
        long semanas = totalMinutos / MINUTOS_SEMANA;
        long restoSemanas = totalMinutos % MINUTOS_SEMANA;

        long dias = restoSemanas / MINUTOS_DIA;
        long minutosFinales = restoSemanas % MINUTOS_DIA;

        // Se calcula el importeTotal dependiendo del tiempo
        importeTotal = calcularTotalImporte(totalMinutos, semanas, dias, minutosFinales);
        importeTotal = Formateador.redondear(importeTotal);
    }
    
    /**
     *
     * @param totalMinutos
     * @param semanas
     * @param dias
     * @param minutosFinales
     * @return
     */
    public static double calcularTotalImporte(long totalMinutos, long semanas, long dias, long minutosFinales) {
        
        double total;
        
        if (totalMinutos < MINUTOS_DIA) {
            // Solo minutos
            total = totalMinutos * PRECIO_MINUTO;

        } else if (totalMinutos < MINUTOS_SEMANA) {
            // Días completos + minutos sueltos
            total = (dias * PRECIO_DIA) + (minutosFinales * PRECIO_MINUTO);

        } else {
            // Semanas + días + minutos. Descuento del 10%
            total = (semanas * PRECIO_SEMANA) + (dias * PRECIO_DIA) + (minutosFinales * PRECIO_MINUTO);
            total *= (1 - DESCUENTO);
        }
        
        return total;
    }
    
    

    // --- GETTERS, SETTERS Y DEMÁS PERSONALIZADOS ---

    /**
     * @param parking
     * @param salida
     */
    public static void getTicketMinImporte(Parking parking, StringBuilder salida) {
        // Ticket con menor importe
        Ticket ticketMinImporte = Collections.min(parking.ticketsCerrados);
        salida.append("- ")
                .append("Ticket con menos importe: ")
                .append(ticketMinImporte.getVehiculo().getMatricula())
                .append(" (")
                .append(ticketMinImporte.getImporteTotal())
                .append("€)\n");
    }

    /**
     * @param parking
     * @param salida
     */
    public static void getTicketMaxImporte(Parking parking, StringBuilder salida) {
        
        // Ticket con maś importe
        Ticket ticketMaxImporte = Collections.max(parking.ticketsCerrados);
        salida.append("- ")
                .append("Ticket con más importe: ")
                .append(ticketMaxImporte.getVehiculo().getMatricula())
                .append(" (")
                .append(ticketMaxImporte.getImporteTotal())
                .append("€)\n");
    }

    /**
     * @return
     */
    public String toStringBasico() {
        return vehiculo.getMatricula() + " (" + entrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm")) + ")";
    }

    

    // --- GETTERS Y SETTERS ---

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public int getNumeroTicket() {
        return numeroTicket;
    }

    public LocalDateTime getSalida() {
        return salida;
    }

    
    public void setNumeroTicket(int numeroTicket) {
        this.numeroTicket = numeroTicket;
    }
    
    public void setSalida(LocalDateTime salida) {
        this.salida = salida;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = Formateador.redondear(importeTotal);
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }
    

    
    // --- MÉTODOS OVERRIDE ---
    
    @Override
    public int compareTo(Ticket t) {
        if (importeTotal != t.importeTotal) {
            return Double.compare(importeTotal, t.importeTotal);
        
        } else {
            return entrada.compareTo(t.entrada);
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy, hh:mm");
        return vehiculo.getMatricula()
                + " ("
                + entrada.format(formatoFecha)
                + ") - ("
                + salida.format(formatoFecha)
                + ") | "
                + importeTotal + "€";
    }

}
