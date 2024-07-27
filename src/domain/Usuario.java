package domain;

public class Usuario {
    private String nombre;
    private int años;
    private String telefono;
    private String contraseña;
    private double saldo;

    public Usuario(String nombre, int años, String telefono, String contraseña, double saldo) {
        this.nombre = nombre;
        this.años = años;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.saldo = saldo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
