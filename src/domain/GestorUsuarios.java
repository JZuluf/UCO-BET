package domain;

import util.ExcepcionesUcoBet;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {
    private List<Usuario> usuarios;

    public GestorUsuarios() {
        this.usuarios = new ArrayList<>();
    }

    public void crearUsuario(String nombre, String telefono, String contraseña, double saldo) throws ExcepcionesUcoBet {
        if (nombre.isEmpty() || telefono.isEmpty() || contraseña.isEmpty()) {
            throw new ExcepcionesUcoBet("Ningún campo debe estar vacío.");
        }
        usuarios.add(new Usuario(nombre, telefono, contraseña, saldo, false));
    }

    public void crearAdministrador(String nombre, String telefono, String contraseña) throws ExcepcionesUcoBet {
        if (nombre.isEmpty() || telefono.isEmpty() || contraseña.isEmpty()) {
            throw new ExcepcionesUcoBet("Ningún campo debe estar vacío.");
        }
        usuarios.add(new Usuario(nombre, telefono, contraseña, 0, true));
    }

    public Usuario autenticarUsuario(String nombre, String contraseña) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(nombre) && usuario.getContraseña().equals(contraseña)) {
                return usuario;
            }
        }
        return null;
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
