package domain;

public class Usuario {
    private String nombre;
    private String telefono;
    private String contraseña;
    private double saldo;
    private boolean esAdmin;

    public Usuario(String nombre, String telefono, String contraseña, double saldo, boolean esAdmin) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.saldo = saldo;
        this.esAdmin = esAdmin;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
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

    public boolean isEsAdmin() {
        return esAdmin;
    }
}
