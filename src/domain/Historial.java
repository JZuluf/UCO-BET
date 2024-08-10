package domain;

import java.util.ArrayList;
import java.util.List;

public class Historial {
    private List<Sorteo> sorteos;

    public Historial() {
        this.sorteos = new ArrayList<>();
    }

    public void agregarSorteo(Sorteo sorteo) {
        sorteos.add(sorteo);
    }

    public List<Sorteo> obtenerSorteos() {
        return sorteos;
    }
}
