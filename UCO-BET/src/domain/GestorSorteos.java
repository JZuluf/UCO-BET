package domain;

import util.ExcepcionesUcoBet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestorSorteos {
    private List<Sorteo> sorteos;

    public GestorSorteos() {
        this.sorteos = new ArrayList<>();
    }

    public void setSorteos(List<Sorteo> sorteos) {
        this.sorteos = sorteos;
    }

    public List<Sorteo> obtenerSorteos() {
        return sorteos;
    }

    public List<Sorteo> obtenerSorteosActivos() {
        List<Sorteo> activos = new ArrayList<>();
        for (Sorteo sorteo : sorteos) {
            if (sorteo.estaActivo()) {
                activos.add(sorteo);
            }
        }
        return activos;
    }

    public void crearSorteo(LocalDateTime fechaHora, int numeroGanador) {
        Sorteo nuevoSorteo = new Sorteo(fechaHora, numeroGanador, true);
        sorteos.add(nuevoSorteo);
    }

    public void agregarApuesta(Sorteo sorteo, Usuario usuario, int numeroApostado, double monto) throws ExcepcionesUcoBet {
        if (!sorteo.estaActivo()) {
            throw new ExcepcionesUcoBet("El sorteo no está activo.");
        }
        Apuesta nuevaApuesta = new Apuesta(usuario, numeroApostado, monto);
        sorteo.agregarApuesta(nuevaApuesta);
    }
}
