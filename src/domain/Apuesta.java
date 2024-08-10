package domain;

public class Apuesta {
    private Usuario usuario;
    private int numeroApostado;
    private double monto;

    public Apuesta(Usuario usuario, int numeroApostado, double monto) {
        this.usuario = usuario;
        this.numeroApostado = numeroApostado;
        this.monto = monto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public int getNumeroApostado() {
        return numeroApostado;
    }

    public double getMonto() {
        return monto;
    }
}
