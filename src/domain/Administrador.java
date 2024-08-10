package domain;

public class Administrador extends Usuario {
    public Administrador(String nombre, String telefono, String contraseña) {
        super(nombre, telefono, contraseña, 0.0, true);
    }
}