/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidadesParking;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author aitorgallegoo_cr
 */
public class Formateador {

    /**
     * Redondea un número a 2 decimales.
     * @param n Número que se le indica para que lo redondee.
     * @return El número redondeado a 2 decimales.
     */
    public static double redondear(double n) {
        return Math.round(n * 100) / 100.0;
    }

    /**
     * Formatea una cadena de texto representando un nombre, asegurando que la primera letra
     * de cada palabra sea mayúscula y el resto minúsculas.
     * Este método procesa nombres compuestos, elimina espacios en blanco adicionales
     * al inicio, al final y entre palabras, y normaliza el uso de mayúsculas
     * en toda la cadena.
     *
     * @param nombre El nombre original introducido por el usuario.
     * @return El nombre formateado con la primera letra de cada palabra en mayúscula.
     */
    public static String formatearNombre(String nombre) {
        return Arrays.stream(nombre.trim().toLowerCase().split("\\s+"))
                .filter(palabra -> !palabra.isEmpty())
                .map(palabra -> Character.toUpperCase(palabra.charAt(0)) + palabra.substring(1))
                .collect(Collectors.joining(" "));
    }

    /**
     * Formatea un texto de forma que lo convierte todo a minúsculas y le quita las tildes a las vocales.
     * 
     * @param textoAFormaterar Texto que se va a formatear.
     * @return Texto ya formateado como se explica.
     */
    public static String formatearTexto(String textoAFormaterar) {
        
        if (textoAFormaterar == null) {
            return "";
        }
        
        // Normaliza el texto y quita los símbolos de acentos (diacríticos)
        String normalizado = Normalizer.normalize(textoAFormaterar, Normalizer.Form.NFD);
        return normalizado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")
                .toLowerCase().trim();
    }

}
