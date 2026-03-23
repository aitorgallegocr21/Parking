/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;

/**
 *
 * @author usumaniana
 */
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // Atributos de instancia
    private final String matricula;
    private final String nombrePropietario;
    private final String marca;
    private final String modelo;
    private final String color;
    private final boolean esAbonado;

    /**
     * Constructor de vehículo.
     * @param matricula
     * @param nombrePropietario
     * @param marca
     * @param modelo
     * @param color
     * @param esAbonado
     */
    public Vehiculo(String matricula, String nombrePropietario, String marca, String modelo, String color, boolean esAbonado) {
        this.matricula = matricula;
        this.nombrePropietario = nombrePropietario;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.esAbonado = esAbonado;
    }
    
    
    
    // --- GETTERS ---

    public String getMatricula() {
        return matricula;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getColor() {
        return color;
    }

    public boolean isEsAbonado() {
        return esAbonado;
    }
    
}
