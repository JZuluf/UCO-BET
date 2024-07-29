package domain;

import java.time.LocalDateTime;

public class Sorteo {
    private LocalDateTime fechaHora;
    private int numeroGanador;
    private boolean activo;

    public Sorteo(LocalDateTime fechaHora, int numeroGanador, boolean activo) {
        this.fechaHora = fechaHora;
        this.numeroGanador = numeroGanador;
        this.activo = activo;
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

    @Override
    public String toString() {
        return "Sorteo{" +
                "fechaHora=" + fechaHora +
                ", numeroGanador=" + numeroGanador +
                ", activo=" + activo +
                '}';
    }
}
