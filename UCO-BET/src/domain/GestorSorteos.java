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

    public void crearSorteo(LocalDateTime fechaHora, int numeroGanador) throws ExcepcionesUcoBet {
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new ExcepcionesUcoBet("La fecha y hora del sorteo deben ser futuras.");
        }
        sorteos.add(new Sorteo(fechaHora, numeroGanador, true));
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

    public List<Sorteo> obtenerHistorialSorteos() {
        List<Sorteo> historial = new ArrayList<>();
        for (Sorteo sorteo : sorteos) {
            if (!sorteo.estaActivo()) {
                historial.add(sorteo);
            }
        }
        return historial;
    }

    public void realizarApuesta(Usuario usuario, Sorteo sorteo, int numero, double monto) throws ExcepcionesUcoBet {
        if (usuario.getSaldo() < monto) {
            throw new ExcepcionesUcoBet("Saldo insuficiente.");
        }

        long minutosRestantes = java.time.Duration.between(LocalDateTime.now(), sorteo.getFechaHora()).toMinutes();
        if (minutosRestantes < 5) {
            throw new ExcepcionesUcoBet("La apuesta debe realizarse al menos 5 minutos antes del sorteo.");
        }

        usuario.setSaldo(usuario.getSaldo() - monto);
        // Aquí puedes agregar la lógica para registrar la apuesta en el sorteo
    }

    public void setSorteos(List<Sorteo> sorteos) {
        this.sorteos = sorteos;
    }

    public List<Sorteo> obtenerSorteos() {
        return sorteos;
    }
}
