package domain;

import java.time.LocalDateTime;

public class Apuesta {
    private Usuario usuario;
    private int numero;
    private double monto;
    private LocalDateTime fechaHora;

    public Apuesta(Usuario usuario, int numero, double monto, LocalDateTime fechaHora) {
        this.usuario = usuario;
        this.numero = numero;
        this.monto = monto;
        this.fechaHora = fechaHora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public int getNumero() {
        return numero;
    }

    public double getMonto() {
        return monto;
    }

    public double calcularGanancia() {
        int numDigits = String.valueOf(numero).length();
        switch (numDigits) {
            case 1:
                return monto * 10;
            case 2:
                return monto * 15;
            case 3:
                return monto * 50;
            case 4:
                return monto * 100;
            default:
                return 0;
        }
    }
}
