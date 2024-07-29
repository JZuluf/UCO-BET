package domain;

import util.ExcepcionesUcoBet;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {
    private List<Usuario> usuarios;

    public GestorUsuarios() {
        this.usuarios = new ArrayList<>();
    }

    public Usuario autenticarUsuario(String nombre, String contraseña) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(nombre) && usuario.getContraseña().equals(contraseña)) {
                return usuario;
            }
        }
        return null;
    }

    public void crearUsuario(String nombre, String telefono, String contraseña, double saldoInicial) throws ExcepcionesUcoBet {
        validarEntrada(nombre, telefono, contraseña);
        if (saldoInicial <= 0) throw new ExcepcionesUcoBet("El saldo inicial debe ser mayor a 0.");
        usuarios.add(new Usuario(nombre, telefono, contraseña, saldoInicial, false));
    }

    public void crearAdministrador(String nombre, String telefono, String contraseña) throws ExcepcionesUcoBet {
        validarEntrada(nombre, telefono, contraseña);
        usuarios.add(new Administrador(nombre, telefono, contraseña));
    }

    public List<Usuario> obtenerUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void recargarSaldo(int indice, double monto) throws ExcepcionesUcoBet {
        if (indice < 0 || indice >= usuarios.size()) throw new ExcepcionesUcoBet("Usuario no encontrado.");
        if (monto <= 0) throw new ExcepcionesUcoBet("El monto debe ser mayor a 0.");
        Usuario usuario = usuarios.get(indice);
        usuario.setSaldo(usuario.getSaldo() + monto);
    }

    public String obtenerListaUsuarios() {
        StringBuilder lista = new StringBuilder("Usuarios:\n");
        for (int i = 0; i < usuarios.size(); i++) {
            lista.append(i).append(". ").append(usuarios.get(i).getNombre())
                    .append(" - Saldo: ").append(usuarios.get(i).getSaldo()).append("\n");
        }
        return lista.toString();
    }

    private void validarEntrada(String nombre, String telefono, String contraseña) throws ExcepcionesUcoBet {
        if (nombre == null || nombre.isEmpty() || telefono == null || telefono.isEmpty() || contraseña == null || contraseña.isEmpty()) {
            throw new ExcepcionesUcoBet("Ningún campo puede estar vacío.");
        }
    }
}
