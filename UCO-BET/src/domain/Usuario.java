package domain;

public class Usuario {
    private String nombre;
    private String telefono;
    private String contraseña;
    private double saldo;

    public Usuario(String nombre, String telefono, String contraseña, double saldo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.saldo = saldo;
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

    @Override
    public String toString() {
        return nombre + "," + telefono + "," + contraseña + "," + saldo;
    }
}
