package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sorteo {
    private LocalDateTime fechaHora;
    private int numeroGanador;
    private boolean finalizado;
    private List<Apuesta> apuestas;

    public Sorteo(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
        this.finalizado = false;
        this.apuestas = new ArrayList<>();
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public int getNumeroGanador() {
        return numeroGanador;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public List<Apuesta> getApuestas() {
        return apuestas;
    }

    public void agregarApuesta(Apuesta apuesta) {
        apuestas.add(apuesta);
    }

    public void finalizarSorteo() {
        Random random = new Random();
        this.numeroGanador = random.nextInt(9999) + 1;
        this.finalizado = true;
    }

    public List<Usuario> obtenerGanadores() {
        List<Usuario> ganadores = new ArrayList<>();
        for (Apuesta apuesta : apuestas) {
            if (apuesta.getNumero() == numeroGanador) {
                ganadores.add(apuesta.getUsuario());
            }
        }
        return ganadores;
    }
}
