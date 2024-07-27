package domain;

public class Apuesta {
    private Usuario usuario;
    private int numero;
    private double monto;

    public Apuesta(Usuario usuario, int numero, double monto) {
        this.usuario = usuario;
        this.numero = numero;
        this.monto = monto;
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
        int longitudNumero = String.valueOf(numero).length();
        switch (longitudNumero) {
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
