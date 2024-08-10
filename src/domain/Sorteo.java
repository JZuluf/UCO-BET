package domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sorteo {
    private LocalDateTime fechaHora;
    private int numeroGanador;
    private boolean activo;
    private List<Apuesta> apuestas;

    public Sorteo(LocalDateTime fechaHora, int numeroGanador, boolean activo) {
        this.fechaHora = fechaHora;
        this.numeroGanador = numeroGanador;
        this.activo = activo;
        this.apuestas = new ArrayList<>();
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public int getNumeroGanador() {
        return numeroGanador;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Apuesta> getApuestas() {
        return apuestas;
    }

    public void agregarApuesta(Apuesta apuesta) {
        this.apuestas.add(apuesta);
    }

    @Override
    public String toString() {
        return "Fecha y hora: " + fechaHora + ", Número ganador: " + numeroGanador + ", Estado: " + (activo ? "Activo" : "Inactivo");
    }

    public String toStringSinNumeroGanador() {
        return "Fecha y hora: " + fechaHora + ", Estado: " + (activo ? "Activo" : "Inactivo");
    }
}
