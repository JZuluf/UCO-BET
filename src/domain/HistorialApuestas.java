package domain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HistorialApuestas {

    private static final String ARCHIVO_APUESTAS = "historial_apuestas.txt";

    public static void registrarApuesta(Usuario usuario, Sorteo sorteo, int numeroApostado, double valorApostado) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_APUESTAS, true))) {
            writer.write(String.format("Usuario.java: %s, Sorteo: %s, Número Apostado: %d, Valor Apostado: %.2f%n",
                    usuario.getNombre(), sorteo, numeroApostado, valorApostado));
        } catch (IOException e) {
            System.err.println("Error al guardar el historial de apuestas: " + e.getMessage());
        }
    }
}
