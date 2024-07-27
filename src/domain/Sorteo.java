package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sorteo {
    private LocalDateTime fechaHora;
    private List<Apuesta> apuestas;
    private Integer numeroGanador;
    private boolean realizado;

    public Sorteo(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
        this.apuestas = new ArrayList<>();
        this.numeroGanador = null;
        this.realizado = false;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public Integer getNumeroGanador() {
        return numeroGanador;
    }

    public void agregarApuesta(Apuesta apuesta) {
        if (!realizado) {
            apuestas.add(apuesta);
        }
    }

    public void realizarSorteo() {
        if (LocalDateTime.now().isAfter(fechaHora) && !realizado) {
            Random rand = new Random();
            numeroGanador = rand.nextInt(9999) + 1;  // Número entre 1 y 9999
            realizado = true;

            for (Apuesta apuesta : apuestas) {
                if (esGanador(apuesta.getNumero())) {
                    double ganancia = apuesta.calcularGanancia();
                    apuesta.getUsuario().setSaldo(apuesta.getUsuario().getSaldo() + ganancia);
                }
            }
        }

    }

    private boolean esGanador(int numeroApostado) {
        // Convertir a cadenas para comparar dígitos
        String numeroGanadorStr = String.format("%04d", numeroGanador); // Formatear con ceros a la izquierda
        String numeroApostadoStr = String.format("%04d", numeroApostado); // Formatear con ceros a la izquierda

        // Comprobar si hay coincidencia de dígitos
        return numeroGanadorStr.equals(numeroApostadoStr);
    }

    public List<Apuesta> obtenerApuestas() {
        return apuestas;
    }
}

